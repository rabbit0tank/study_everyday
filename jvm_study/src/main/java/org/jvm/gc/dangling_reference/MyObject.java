package org.jvm.gc.dangling_reference;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liqh
 */
@Getter
public class MyObject {
    private final String value = "Hello";

    private final List<Object> memoryLeakList = new ArrayList<>();

    public MyObject() {
        memoryLeak();
    }
    private  void memoryLeak()  {
        for (int i = 0; i < 6095; i++) {
            // 创建一个大对象并添加到 List 中
            // 1MB 大小的对象
            Object largeObject = new byte[1024 * 1024];
            memoryLeakList.add(largeObject);
//            System.out.println("Added object " + i);

            // 每添加一定数量的对象后输出当前的内存使用情况
            if (i % 1000 == 0) {
                printMemoryUsage();
            }
        }

        // 运行一段时间，让我们可以查看内存使用情况
            printMemoryUsage();
    }

    private static void printMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long totalMemory = runtime.totalMemory();
        long maxMemory = runtime.maxMemory();

        System.out.println("Used Memory: " + usedMemory / (1024 * 1024) + " MB");
        System.out.println("Total Memory: " + totalMemory / (1024 * 1024) + " MB");
        System.out.println("Max Memory: " + maxMemory / (1024 * 1024) + " MB");
        System.out.println("---------------------------");
    }
}
