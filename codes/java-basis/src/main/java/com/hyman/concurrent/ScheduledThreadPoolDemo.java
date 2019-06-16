package com.hyman.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/16 10:22
 * @version： 1.0.0
 */
public class ScheduledThreadPoolDemo {

    public static void main(String[] args) {
        delay();
        cycle();
    }

    private static void cycle() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            System.out.println(Thread.currentThread().getName() + " 延迟 1 秒，每 3 秒执行一次");
        }, 1, 3, TimeUnit.SECONDS);
    }

    private static void delay() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.schedule(() -> {
                    System.out.println(Thread.currentThread().getName() + " 延迟 3 秒");
                }, 3,
                TimeUnit.SECONDS);
    }


}
