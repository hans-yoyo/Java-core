package com.hyman.javabasis.reflect;

import java.util.HashSet;
import java.util.Set;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/01 21:11
 * @version： 1.0.0
 */
public class ReflectClassDemo03 {
    enum E {A, B}

    public static void main(String[] args) {
        Class c = "foo".getClass();
        System.out.println(c.getCanonicalName());

        Class c2 = ReflectClassDemo03.E.A.getClass();
        System.out.println(c2.getCanonicalName());

        byte[] bytes = new byte[1024];
        Class c3 = bytes.getClass();
        System.out.println(c3.getCanonicalName());

        Set<String> set = new HashSet<>();
        Class c4 = set.getClass();
        System.out.println(c4.getCanonicalName());
    }
}
