package com.hyman.advance.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/02 11:04
 * @version： 1.0.0
 */
public class RegexDemo {

    public static void main(String[] args) {
        checkLookingAt("hello", "helloworld");
        checkLookingAt("world", "helloworld");

        checkFind("hello", "helloworld");
        checkFind("world", "helloworld");

        checkMatches("hello", "helloworld");
        checkMatches("world", "helloworld");
        checkMatches("helloworld", "helloworld");
    }

    private static void checkLookingAt(String regex, String content) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.lookingAt()) {
            System.out.println(content + "\tlookingAt： " + regex);
        } else {
            System.out.println(content + "\tnot lookingAt： " + regex);
        }
    }

    private static void checkFind(String regex, String content) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.find()) {
            System.out.println(content + "\tfind： " + regex);
        } else {
            System.out.println(content + "\tnot find： " + regex);
        }
    }

    private static void checkMatches(String regex, String content) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        if (m.matches()) {
            System.out.println(content + "\tmatches： " + regex);
        } else {
            System.out.println(content + "\tnot matches： " + regex);
        }
    }

}
