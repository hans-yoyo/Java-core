package com.hyman.io;

import java.io.*;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/09 12:36
 * @version： 1.0.0
 */
public class OutputStreamWriterDemo {

    public static void main(String[] args) {
        File f = new File("d:" + File.separator + "filetest.txt");
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(f));
            out.write("Hello Java World");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
