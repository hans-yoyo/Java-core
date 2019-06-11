package com.hyman.concurrent;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/11 22:31
 * @version： 1.0.0
 */
public class RunnableDemo01 {

    public static void main(String[] args) {
        MyThread th = new MyThread("线程A");
        new Thread(th).start();
        new Thread(th).start();
        new Thread(th).start();
    }

    static class MyThread implements Runnable {

        private int ticket = 5;
        private String name;

        MyThread(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 100; i++) {
                if (this.ticket > 0) {
                    System.out.println(this.name + " 卖票：ticket = " + ticket--);
                }
            }
        }
    }

}
