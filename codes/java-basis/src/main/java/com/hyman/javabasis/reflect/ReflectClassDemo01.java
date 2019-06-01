package com.hyman.javabasis.reflect;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/01 17:06
 * @version： 1.0.0
 */
public class ReflectClassDemo01 {
    public static void main(String[] args) {
        try {
            Class c1 = Class.forName("com.hyman.javabasis.reflect.ReflectClassDemo01");
            System.out.println(c1.getCanonicalName());// com.hyman.javabasis.reflect.ReflectClassDemo01

            Class c2 = Class.forName("[D");
            System.out.println(c2.getCanonicalName()); // double[]

            Class c3 = Class.forName("[[Ljava.lang.String;");
            System.out.println(c3.getCanonicalName()); // java.lang.String[][]
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
