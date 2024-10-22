package org.jvm.gc.mark_sweep;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class MarkAndSweep {
    private Set<SimpleObject> markedObjects = new HashSet<>();
    private List<SimpleObject> heap = new ArrayList<>();

    public void allocate(SimpleObject obj) {
        heap.add(obj);
    }

    public void mark(SimpleObject root) {
        if (root == null || markedObjects.contains(root)) {
            return;
        }
        markedObjects.add(root);
        for (SimpleObject ref : root.references) {
            mark(ref);
        }
    }

    public void sweep() {
        heap.removeIf(obj -> !markedObjects.contains(obj));
        markedObjects.clear(); // 清除标记
    }

    public static void main(String[] args) {
        MarkAndSweep gc = new MarkAndSweep();

        // 创建对象
        SimpleObject objA = new SimpleObject("A");
        SimpleObject objB = new SimpleObject("B");
        SimpleObject objC = new SimpleObject("C");
        SimpleObject objD = new SimpleObject("D");

        // 设置引用关系
        objA.addReference(objB);
        objB.addReference(objC);
        // objD 不被引用

        // 分配对象
        gc.allocate(objA);
        gc.allocate(objB);
        gc.allocate(objC);
        gc.allocate(objD);

        // 模拟 GC
        System.out.println("Before GC:");
        gc.heap.forEach(System.out::println);

        // 从根对象开始标记
        gc.mark(objA);

        // 执行清除
        gc.sweep();

        System.out.println("After GC:");
        gc.heap.forEach(System.out::println);
    }
}
