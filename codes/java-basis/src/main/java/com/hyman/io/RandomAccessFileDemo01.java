package com.hyman.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/09 11:53
 * @version： 1.0.0
 */
public class RandomAccessFileDemo01 {

    public static void main(String[] args) {
        File f = new File("D:" + File.separator + "filetest.txt");
        RandomAccessFile rdf = null;
        try {
            rdf = new RandomAccessFile(f, "rw");
            rdf.writeBytes("zhangsan");
            rdf.writeBytes("18");
            rdf.writeBytes("lisi");
            rdf.writeBytes("20");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rdf != null) {
                    rdf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
