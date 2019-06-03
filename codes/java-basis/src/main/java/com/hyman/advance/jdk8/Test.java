package com.hyman.advance.jdk8;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/03 21:59
 * @version： 1.0.0
 */
public class Test {

    public static void main(String[] args) {
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };

        List list = new ArrayList();
        System.out.println(formula.calculate(100));
        // 直接使用默认的sqrt方法
        System.out.println(formula.sqrt(64));
    }

}
