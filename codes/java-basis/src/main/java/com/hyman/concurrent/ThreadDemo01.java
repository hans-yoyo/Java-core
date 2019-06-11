package com.hyman.concurrent;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/11 22:25
 * @version： 1.0.0
 */
public class ThreadDemo01 {
    public static void main(String[] args) {
        Thread01 mt01 = new Thread01("线程A");
        Thread01 mt02 = new Thread01("线程B");
        mt01.start();
        mt02.start();
    }

    static class Thread01 extends Thread {
        private int ticket = 5;

        Thread01(String name) {
            super(name);
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                if (this.ticket > 0) {
                    System.out.println(this.getName() + " 卖票：ticket = " + ticket--);
                }
            }
        }
    }
}
