package com.hyman.advance.regex;

import org.junit.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/02 11:33
 * @version： 1.0.0
 */
public class RegexDemo03 {

    public static void main(String[] args) {
        Assert.assertTrue(checkMatches("yes|no", "yes"));
        Assert.assertTrue(checkMatches("yes|no", "no"));
        Assert.assertFalse(checkMatches("yes|no", "right"));

        Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "ended"));
        Assert.assertTrue(checkMatches("(play|end)(ing|ed)", "ending"));

        Assert.assertTrue(checkMatches("[abc]", "b"));  // 字符只能是a、b、c
        Assert.assertTrue(checkMatches("[a-z]", "m")); // 字符只能是a - z
        Assert.assertTrue(checkMatches("[A-Z]", "O")); // 字符只能是A - Z
        Assert.assertTrue(checkMatches("[a-zA-Z]", "K")); // 字符只能是a - z和A - Z
        Assert.assertTrue(checkMatches("[a-zA-Z]", "k"));
        Assert.assertTrue(checkMatches("[0-9]", "5")); // 字符只能是0 - 9

        Assert.assertFalse(checkMatches("[^abc]", "b")); // 字符不能是a、b、c
        Assert.assertFalse(checkMatches("[^a-z]", "m")); // 字符不能是a - z
        Assert.assertFalse(checkMatches("[^A-Z]", "O")); // 字符不能是A - Z

        // {n}: n 是一个非负整数。匹配确定的 n 次。
        Assert.assertFalse(checkMatches("ap{1}", "a"));
        Assert.assertTrue(checkMatches("ap{1}", "ap"));
        Assert.assertFalse(checkMatches("ap{1}", "app"));

        // {n,}: n 是一个非负整数。至少匹配 n 次。
        Assert.assertFalse(checkMatches("ap{1,}", "a"));
        Assert.assertTrue(checkMatches("ap{1,}", "ap"));
        Assert.assertTrue(checkMatches("ap{1,}", "app"));

        // {n,m}: m 和 n 均为非负整数，其中 n <= m。最少匹配 n 次且最多匹配 m 次。
        Assert.assertFalse(checkMatches("ap{2,5}", "a"));
        Assert.assertTrue(checkMatches("ap{2,5}", "app"));
        Assert.assertFalse(checkMatches("ap{2,5}", "apppppppppp"));
    }

    public static boolean checkMatches(String regex, String content) {
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(content);
        boolean matches = matcher.matches();
        if (matches) {
            System.out.println(content + " matches " + regex);
        } else {
            System.out.println(content + " not matches " + regex);
        }
        return matches;
    }

}
