package org.jvm.gc.performance;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.List;

/**
 * @author liqh
 */
public class ProfermanceTest {
    private static long startTime;
    private static GcMonitor gcMonitor;


    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        // 启动一个线程来监控 GC
        gcMonitor =new GcMonitor();
        gcMonitor.setDaemon(true);
        gcMonitor.start();



        // 模拟对象创建和使用
        simulateWorkload();
        System.gc();

        // 打印性能指标
        printPerformanceMetrics();

    }

    private static void simulateWorkload() {
        // 模拟工作负载，创建大量对象
        for (int i = 0; i < 100000; i++) {
            String str = "Object " + i;
            // 访问对象，模拟工作
            if (i % 1000 == 0) {
                System.out.println("Created: " + str);
            }
        }
    }



    private static void printPerformanceMetrics() {
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        // 吞吐量,实际请求所用时间所占比例
        double throughput = (double) (totalTime - gcMonitor.getTotalGcTime()) / totalTime;

        // 堆使用效率
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        long usedHeap = heapMemoryUsage.getUsed();
        long maxHeap = heapMemoryUsage.getMax();
        double heapUsageEfficiency = (double) usedHeap / maxHeap;

        System.out.println("Total Time: " + totalTime + " ms");
        System.out.println("Total GC Time: " + gcMonitor.getTotalGcTime() + " ms");
        System.out.println("Max Pause Time: " + gcMonitor.getMaxPauseTime() + " ms");
        System.out.println("Heap Usage Efficiency: " + heapUsageEfficiency);
        System.out.println("Throughput: " +((double) totalTime - gcMonitor.getLastGcTime()) / totalTime);

    }
}
