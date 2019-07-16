package com.hyman.advance.jdk8;

/**
 * @描述：
 * @Author：huhan
 * @Date: 2019/06/03 21:57
 */
interface Formula {
    double calculate(int a);

    default double sqrt(int a) {
        return Math.sqrt(a);
    }

    static void staticMethod(){
        System.out.println("This is static Method in interface");
    }
}
