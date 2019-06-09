package com.hyman.io;

import java.io.File;
import java.io.IOException;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/06/08 23:12
 * @version： 1.0.0
 */
public class FileDemo {

    public static void main(String[] args) {
        String path = "D:" + File.separator + "fileseparator.txt";
        System.out.printf("File Path is %s.", path);
        File file = new File(path);
        try {
            boolean b = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
