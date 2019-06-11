package com.hyman.concurrent;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/11 22:36
 * @version： 1.0.0
 */
public class CallableAndFutureDemo {

    public static void main(String[] args) {
        Callable<Integer> callable = () -> new Random().nextInt(100);
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        new Thread(futureTask).start();
        try {
            Thread.sleep(1000);
            System.out.println(futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
