package com.hyman.advance.jdk8;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/07/16 09:11
 * @version： 1.0.0
 */
public class Java8Test {

    public static void main(String[] args) {
        Java8Test tester = new Java8Test();

        // 类型申明
        MathOperation addition = (int a, int b) -> a + b;

        // 等价于
        MathOperation add = new MathOperation() {
            @Override
            public int operation(int a, int b) {
                return a + b;
            }
        };

        // 省略类型申明
        MathOperation subtraction = (int a, int b) -> a - b;

        // 大括号中的返回语句
        MathOperation multiplication = (int a, int b) -> {
            return a * b;
        };

        // 没有大括号，返回语句
        MathOperation division = (int a, int b) -> a / b;

        System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
        System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
        System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
        System.out.println("10 / 5 = " + tester.operate(10, 5, division));

        // 不用括号
        GreetingService greetService1 = message ->
                System.out.println("Hello " + message);

        // 用括号
        GreetingService greetService2 = (message) ->
                System.out.println("Hello " + message);

        greetService1.sayMessage("Runoob");
        greetService2.sayMessage("Google");

    }


    @java.lang.FunctionalInterface
    interface MathOperation {
        int operation(int a, int b);
    }

    @java.lang.FunctionalInterface
    interface GreetingService {
        void sayMessage(String message);
    }

    private int operate(int a, int b, MathOperation mathOperation) {
        return mathOperation.operation(a, b);
    }
}
