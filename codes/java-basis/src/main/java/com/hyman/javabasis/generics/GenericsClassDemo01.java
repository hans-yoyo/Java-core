package com.hyman.javabasis.generics;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/01 16:00
 * @version： 1.0.0
 */
public class GenericsClassDemo01 {
    public static void main(String[] args) {
        Info<Integer> info = new Info<>();
        info.setValue(0);
        System.out.println(info.getValue());

        Info<String> info1 = new Info<>();
        info1.setValue("hello world");
        System.out.println(info1.getValue());
    }
}
