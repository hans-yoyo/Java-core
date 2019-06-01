package com.hyman.javabasis.generics;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/01 16:19
 * @version： 1.0.0
 */
public class GenericsMethodDemo01 {

    public static <T> void printClass(T obj) {
        System.out.println(obj.getClass().toString());
    }

    public static void main(String[] args) {
        printClass("abc");
        printClass(10);
    }

}
