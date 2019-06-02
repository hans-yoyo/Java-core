package com.hyman.advance.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/02 11:21
 * @version： 1.0.0
 */
public class RegexDemo01 {
    public static void main(String[] args) {
        final String regex = "world";
        final String content = "helloworld helloworld";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        System.out.println("content: " + content);

        int i = 0;
        while (m.find()) {
            i++;
            System.out.println("[" + i + "th] found");
            System.out.print("start: " + m.start() + ", ");
            System.out.print("end: " + m.end() + ", ");
            System.out.print("group: " + m.group() + "\n");
        }
    }
}
