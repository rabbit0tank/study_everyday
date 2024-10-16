package org.jvm.gc.memory_leak;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class MemoryLeakExample {
    // 使用静态 List 来保持对对象的引用
    private static final List<Object> MEMORY_LEAK_LIST = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 5000; i++) {
            // 创建一个大对象并添加到 List 中
            Object largeObject = new byte[1024 * 1024]; // 1MB 大小的对象
            MEMORY_LEAK_LIST.add(largeObject);
//            System.out.println("Added object " + i);

            // 每添加一定数量的对象后输出当前的内存使用情况
            if (i % 1000 == 0) {
                printMemoryUsage();
            }
        }

        // 运行一段时间，让我们可以查看内存使用情况
        try {
            printMemoryUsage();
            Thread.sleep(30000); // 30秒
            System.out.println("-------------After 30 seconds----------");
            printMemoryUsage();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
