package com.hyman.advance.encode;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要，其实就是将需要摘要的数据作为参数，经过哈希函数(Hash)的计算，得到的散列值。
 *
 * @Description:
 * @author: Hyman
 * @date: 2019/06/02 18:27
 * @version： 1.0.0
 */
public class MsgDigestDemo {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String msg = "Hello Java";

        MessageDigest md5Digest = MessageDigest.getInstance("MD5");
        md5Digest.update(msg.getBytes());
        byte[] md5Encoded = md5Digest.digest();

        MessageDigest shaDigest = MessageDigest.getInstance("SHA");
        shaDigest.update(msg.getBytes());
        byte[] shaEncoded = shaDigest.digest();

        System.out.println("原文：" + msg);
        System.out.println("MD5摘要： " + Base64.encodeBase64URLSafeString(md5Encoded));
        System.out.println("SHA摘要： " + Base64.encodeBase64URLSafeString(shaEncoded));
    }

}
