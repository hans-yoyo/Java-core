package com.hyman.advance.jdk8;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/07/17 20:34
 * @version： 1.0.0
 */
public class DateApiTest {

    public static void main(String[] args) {
        // LocalDate:表示一个具体的日期，但不包含具体时间，也不包含时区信息。
        LocalDate localDate = LocalDate.of(2019, 7, 17);
        System.out.println(localDate);// 2019-07-17
        System.out.println(localDate.getYear());// 2019
        System.out.println(localDate.getMonthValue());// 7
        System.out.println(localDate.getDayOfMonth());// 17
        System.out.println(localDate.isLeapYear());// false
        System.out.println(LocalDate.now());// 2019-07-17

        // LocalTime:LocalTime和LocalDate类似，他们之间的区别在于LocalDate不包含具体时间，而LocalTime包含具体时间
        System.out.println(LocalTime.now());
        System.out.println(LocalTime.of(12, 30, 45));

        // LocalDateTime：LocalDateTime类是LocalDate和LocalTime的结合体
        // 通过LocalTime.of
        System.out.println(LocalDateTime.of(2019, 07, 17, 20, 44, 57));
        // LocalDate和LocalTime合并
        LocalDate localDate1 = LocalDate.of(2019, Month.JANUARY, 12);
        LocalTime localTime = LocalTime.of(12, 13, 14);
        LocalDateTime localDateTime = localDate1.atTime(localTime);
        System.out.println(localDateTime.toLocalDate());


        // Instant:同System.currentTimeMillis(),Instant可以精确到纳秒
        Instant now = Instant.now();

        // Duration：Duration的内部实现与Instant类似，Duration表示一个时间段
        Duration duration = Duration.between(LocalDateTime.of(2019, 12, 12, 13, 23, 24),
                LocalDateTime.of(2018, Month.APRIL, 23, 14, 23, 34));
        System.out.println(duration.toDays());// -597
        System.out.println(duration.toMinutes());// -861059
        // Duration.of()创建
        Duration.of(2, ChronoUnit.HOURS);

        // Period:在概念上和Duration类似，区别在于Period是以年月日来衡量一个时间段，比如2年3个月6天
        Period.of(2, 3, 4);

        // 增加、减少日期
        LocalDateTime localDateTime1 = LocalDateTime.now();
        LocalDateTime ldt2 = localDateTime1.plusDays(3);
        System.out.println(ldt2);
        LocalDateTime ldt3 = ldt2.plus(2, ChronoUnit.HOURS);
        System.out.println(ldt3);

        // 格式化日期
        // Date格式化String
        String date1 = LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);// 20190717
        System.out.println(date1);
        String date2 = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);//2019-07-17
        System.out.println(date2);
        String date3 = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);//2019-07-17T21:26:09.622
        System.out.println(date3);
        String date4 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));//2019-07-17 21:26:09
        System.out.println(date4);
        String strDate5 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("今天是：YYYY年 MM月 dd日 E", Locale.CHINESE)); // 今天是：2019年 07月 17日 星期三
        System.out.println(strDate5);
        // String转Date
        String strDate6 = "2017-01-05";
        String strDate7 = "2017-01-05 12:30:05";
        LocalDate date6 = LocalDate.parse(strDate6, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDateTime ldt7 = LocalDateTime.parse(strDate7, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 时区ZoneId


        // Date与Instant互转
        Instant instant = Instant.now();
        Date d1 = Date.from(instant);
        Instant toInstant = d1.toInstant();

        // Date与LocalDateTime互转
        Date date = Date.from(ldt3.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(date);
        Date n2 = new Date();
        LocalDateTime ldt4 = LocalDateTime.ofInstant(n2.toInstant(), ZoneId.systemDefault());

        // LocalDate转Date：LocalDate不包含时间，转Date时默认转为当天o点
        LocalDate ld = LocalDate.now();
        Date n3 = Date.from(ld.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(n3);

        // 与时间戳互转
        long millis = System.currentTimeMillis();
        LocalDateTime ldt5 = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println(ldt5);
    }

}
