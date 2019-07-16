package com.hyman.advance.jdk8;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/07/16 08:18
 * @version： 1.0.0
 */
public class DefaultMethodTest {

    public static void main(String[] args) {
        Formula formula = new Formula() {
            @Override
            public double calculate(int a) {
                return sqrt(a * 100);
            }
        };
        System.out.println(formula.calculate(100));//100.0
        // 直接使用默认的sqrt方法
        System.out.println(formula.sqrt(64));//8.0

        Formula.staticMethod();
    }

}
