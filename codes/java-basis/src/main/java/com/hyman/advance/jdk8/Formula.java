package com.hyman.advance.jdk8;

/**
 * @描述：
 * @Author：huhan
 * @Date: 2019/06/03 21:57
 */
public interface Formula {

    double calculate(int a);

    /**
     * 通过使用 `default` 关键字将非抽象方法实现添加到接口
     *
     * @param a
     * @return
     */
    default double sqrt(int a) {
        return Math.sqrt(a);
    }

}
