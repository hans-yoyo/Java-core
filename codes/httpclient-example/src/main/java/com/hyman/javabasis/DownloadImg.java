package com.hyman.javabasis;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @author: Hyman
 * @date: 2019/05/29 13:35
 * @version： 1.0.0
 */
public class DownloadImg {

    public static void writeImgEntityToFile(HttpEntity imgEntity, String fileAddress) {
        File storeFile = new File(fileAddress);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(storeFile);

            if (imgEntity != null) {
                InputStream instream;
                instream = imgEntity.getContent();
                byte b[] = new byte[8 * 1024];
                int count;
                while ((count = instream.read(b)) != -1) {
                    output.write(b, 0, count);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        String imgPath = (args != null && args.length > 0) ? args[0] : null;
        System.out.println("获取Bing图片地址中……");
        String host = "http://cn.bing.com";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(host);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            Pattern p = Pattern.compile("g_img=\\{url:.*\\.jpg");
            Matcher m = p.matcher(EntityUtils.toString(response.getEntity()));
            String address = null;
            if (m.find()) {
//                address = host + m.group().split("\"")[1].split("\"")[0];
                address = host + m.group().split("\"")[1].split("\"")[0].replaceAll("\\\\u0026", "&");
            } else {
                System.exit(0);
            }
            System.out.println("图片地址:" + address);
            System.out.println("正在下载……");
            HttpGet getImage = new HttpGet(URLDecoder.decode(address, "utf-8"));
            CloseableHttpResponse responseImg = httpClient.execute(getImage);
            HttpEntity entity = responseImg.getEntity();


            writeImgEntityToFile(entity, ((imgPath == null || imgPath.length() <= 0) ? "D:/data/BingImg/" : imgPath) + dateFormat.format(new Date()) + ".jpg");

            System.out.println("下载完毕.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
