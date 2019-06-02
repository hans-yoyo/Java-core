package com.hyman.advance.encode;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/02 16:18
 * @version： 1.0.0
 */
public class BASE64Demo {

    public static void main(String[] args) {
        String str = "Iamashabi";
        try {
            String base64String = Base64.encodeBase64String(str.getBytes("UTF-8"));
            System.out.println(str + " base64加密后字符串为： " + base64String);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
