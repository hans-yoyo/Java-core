package com.hyman.io;

import java.io.*;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/09 12:26
 * @version： 1.0.0
 */
public class ReaderAndWriterDemo {

    public static void main(String[] args) throws IOException {
        String filePath = "d:\\filetest.txt";
        output(filePath);
        System.out.println("写入内容为" + new String(input(filePath)));
    }

    public static void output(String filePath) throws IOException {
        File f = new File(filePath);
        Writer out = new FileWriter(f);
        out.write("Hello World");
        out.flush();
        out.close();
    }

    public static char[] input(String filePath) throws IOException {
        File f = new File(filePath);
        Reader reader = new FileReader(f);
        int temp = 0;
        int len = 0;
        char[] c = new char[1024];
        while ((temp = reader.read()) != -1) {
            c[len] = (char) temp;
            len++;
        }
        System.out.println("文件字符数为：" + len);
        reader.close();
        return c;
    }

}
