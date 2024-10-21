package org.jvm.gc.performance;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * @author liqh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class GcMonitor extends Thread{
    private long totalGcTime = 0;
    private long maxPauseTime = 0;
    private long lastGcTime = 0;

    @Override
    public void run() {
        monitorGc();
    }

    private  void monitorGc() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        while (true) {
            for (GarbageCollectorMXBean gcBean : gcBeans) {
                long gcCount = gcBean.getCollectionCount();
                long gcTime = gcBean.getCollectionTime();

                if (gcCount > 0) {
                    long pauseTime = gcTime - lastGcTime;
                    if (pauseTime > maxPauseTime) {
                        maxPauseTime = pauseTime;
                    }
                    lastGcTime = gcTime;
                    totalGcTime += gcTime;
                }
            }
            try {
                // 每秒监控一次
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
