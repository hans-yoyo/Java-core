package com.hyman.advance.locale;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/03 21:15
 * @version： 1.0.0
 */
public class DateFormatDemo {

    public static void main(String[] args) {
        Date date = new Date();
        DateFormat dateFormat1 = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        DateFormat dateFormat2 = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
        System.out.format("%s 的本地化（%s）结果: %s\n", date, Locale.SIMPLIFIED_CHINESE, dateFormat1.format(date));
        System.out.format("%s 的本地化（%s）结果: %s\n", date, Locale.SIMPLIFIED_CHINESE, dateFormat2.format(date));
    }

}
