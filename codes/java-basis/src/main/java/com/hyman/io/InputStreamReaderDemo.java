package com.hyman.io;

import java.io.*;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/09 12:40
 * @version： 1.0.0
 */
public class InputStreamReaderDemo {

    public static void main(String[] args) {
        File f = new File("d:" + File.separator + "filetest.txt");
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(f));
            char[] c = new char[1024];
            int len = reader.read(c);
            reader.close();
            System.out.println(new String(c, 0, len));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
