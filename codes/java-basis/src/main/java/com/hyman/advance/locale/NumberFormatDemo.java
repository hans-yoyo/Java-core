package com.hyman.advance.locale;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/03 21:12
 * @version： 1.0.0
 */
public class NumberFormatDemo {

    public static void main(String[] args) {
        double num = 123456.78;
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.SIMPLIFIED_CHINESE);
        System.out.printf("%f 的本地化 (%s) 结果：%s", num, Locale.SIMPLIFIED_CHINESE, format.format(num));
    }

}
