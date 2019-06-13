package com.hyman.concurrent;

import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/13 21:44
 * @version： 1.0.0
 */
public class ReentrantLockDemo {

     private ArrayList<Integer> arrayList = new ArrayList<Integer>();

     // 默认非公平锁
     private Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        final ReentrantLockDemo demo = new ReentrantLockDemo();
        new Thread(()->demo.insert(Thread.currentThread())).start();
        new Thread(()->demo.insert(Thread.currentThread())).start();
    }

    private void insert(Thread thread){
        lock.lock();
        try {
            System.out.println(thread.getName() + "得到了锁");
            for (int i = 0; i < 5; i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println(thread.getName() + "释放了锁");
            lock.unlock();
        }
    }


}
