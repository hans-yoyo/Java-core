package com.hyman.advance.encode;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/02 16:25
 * @version： 1.0.0
 */
public class BASE64Demo01 {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String url = "https://www.baidu.com/s?wd=Base64&rsv_spt=1&rsv_iqid=0xa9188d560005131f&issp=1&f=3&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_sug3=1&rsv_sug1=1&rsv_sug7=001&rsv_sug2=1&rsp=0&rsv_sug9=es_2_1&rsv_sug4=2153&rsv_sug=9";
         byte[] encoded = Base64.encodeBase64(url.getBytes("UTF8")); // 标准的Base64编码
        byte[] encodedUrlSafe = Base64.encodeBase64URLSafe(url.getBytes("UTF8")); // URL安全的Base64编码
        byte[] decoded = Base64.decodeBase64(encoded);
        System.out.println("url:" + url);
        System.out.println("encoded:" + new String(encoded));
        System.out.println("encodedUrlSafe:" + new String(encodedUrlSafe));
        System.out.println("decoded:" + new String(decoded));
    }

}
