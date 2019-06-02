package com.hyman.advance.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/02 11:30
 * @version： 1.0.0
 */
public class RegexDemo02 {

    public static void main(String[] args) {
        String regex = "can";
        String replace = "can not";
        String content = "I can because I think I can.";
        StringBuffer sb = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();

        System.out.println("content: " + content);
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        while (m.find()) {
            m.appendReplacement(sb, replace);
        }
        System.out.println("appendReplacement: " + sb);
        m.appendTail(sb);
        System.out.println("appendTail: " + sb);
    }

}
