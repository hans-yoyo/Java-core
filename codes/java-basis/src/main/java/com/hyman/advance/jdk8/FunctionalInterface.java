package com.hyman.advance.jdk8;

/**
 * @描述：
 * @Author：huhan
 * @Date: 2019/07/16 08:55
 */
@java.lang.FunctionalInterface
public interface FunctionalInterface {

    void handle();

//    void play();

    default void run() {
        System.out.println("Run!");
    }

}
