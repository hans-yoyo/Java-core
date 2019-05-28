package com.hyman.javabasis;

import java.util.ArrayList;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/05/28 16:31
 * @version： 1.0.0
 */
public class OutOfMemoryErrorDemo {
    public static void main(String[] args) {
        ArrayList list = new ArrayList();
        while (true){
            list.add("out of memory");
        }
    }
}
