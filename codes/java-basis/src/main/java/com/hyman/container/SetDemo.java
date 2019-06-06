package com.hyman.container;

import java.util.HashSet;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/06 21:15
 * @version： 1.0.0
 */
public class SetDemo {

    public static void main(String[] args) {
        HashSet<String> set = new HashSet<>();
        set.add("hello world");
        set.add("hello world1");
        set.add("hello world");
    }

}
