package org.jvm.gc.dangling_reference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * @author liqh
 */
public class DanglingReferenceExample {

    public static void main(String[] args) throws InterruptedException {
        // 创建对象
        MyObject obj = new MyObject();
        // 输出: Hello
        System.out.println("Object value: " + obj.getValue());

        // 将对象引用设为 null
        obj = null;

        weakRef();

        // 尝试访问 null 引用会引发 NullPointerException
        // 不安全的操作
        //        System.out.println("Object value: " + obj.getValue());

        /*
           虽然 Java 不会出现真正的悬垂指针(是用了垃圾回收机制)，但以下方法可以避免类似的问题：
            1. 使用局部变量：避免在方法中使用全局变量，确保变量在使用前是有效的。
            2.检查 null 值：在使用对象之前检查引用是否为 null。
         */
//        if (obj != null) {
//            System.out.println("Object value: " + obj.getValue());
//        } else {
//            System.out.println("Object is null.");
//        }
    }

    /**
     * WeakReference 允许垃圾回收器在没有强引用的情况下回收对象。
     * 这意味着，如果没有其他强引用指向对象，WeakReference 可以被清除，从而导致对该对象的引用变为无效。
     *
     *
     * 不被回收的可能原因：
     * JVM 内部优化：
     * 1. 在高内存使用情况下，JVM 可能选择不立即回收所有未使用的对象，以避免频繁的内存分配和释放。即使在调用 System.gc() 时，某些对象可能仍然被保留。
     * 对象被其他部分引用：
     * 2. 确保在你的代码中没有其他地方仍然持有对 MyObject 的强引用。虽然在示例中 obj 被设置为 null，但如果在其他地方存在引用，JVM 会保持该对象的存在。
     * GC 策略：
     * 不同的垃圾回收算法（如 G1、CMS 等）在处理对象时可能具有不同的策略。G1 收集器可能在内存压力较大的情况下选择延迟回收某些对象。
     * GC 频率：
     * 如果 JVM 认为内存使用在可接受范围内，它可能不会回收内存。即使你多次调用 System.gc()，如果没有内存压力，GC 也可能不会回收。
     */
    private static void weakRef() throws InterruptedException {
        MyObject obj = new MyObject();
        WeakReference<MyObject> weakRef = new WeakReference<>(obj);
        // 清除强引用
        obj = null;

        // 给系统提供建议触发垃圾回收
//        System.gc();

        // 可能返回 null
        MyObject retrievedObj = weakRef.get();
        if (retrievedObj != null) {
            System.out.println("Object value: " + retrievedObj.getValue());
        } else {
            System.out.println("Object has been garbage collected.");
        }

//        如果没有取消该引用，会被GC认为是对MyObject仍然有的强引用，造成悬垂引用
        retrievedObj=null;
        System.out.println("Before gc Object value: " + weakRef.get().getValue());

        // 给系统提供建议触发垃圾回收

        // 可能返回 null

        /*
            没有回收的原因：
            1. JVM 行为：垃圾回收是非确定性的。即使调用了 System.gc()，也不一定会立即回收没有强引用的对象。JVM 可能出于性能优化或内存管理的考虑，选择不进行回收。
            2. 内存状态：如果 JVM 认为当前内存状况良好，可能不会对对象进行回收。
            3.JVM 实现：不同的 JVM 实现可能有不同的垃圾回收策略。某些实现可能更倾向于延迟回收对象。
         */
        for(int i =0; i < 10; i++){
            System.gc();
            MyObject retrievedObjAfterGc = weakRef.get();
            if (retrievedObj != null) {
                System.out.println("Object value: " + retrievedObjAfterGc.getValue());

            } else {
                System.out.println("Object has been garbage collected.");
            }
            Thread.sleep(2000);
        }
    }


}

