package com.hyman.javabasis;

import org.junit.Test;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/05/28 16:24
 * @version： 1.0.0
 */
public class StackOverflowErrorDemo {

    private int index = 1;

    public void apply() {
        index++;
        apply();
    }

    @Test
    public void testStackOverflow() {
        try {
            apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
