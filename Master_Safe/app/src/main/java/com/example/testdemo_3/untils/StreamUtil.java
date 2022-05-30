package com.example.testdemo_3.untils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    public static String  streamToString(InputStream is) {
        //1.在读取的过程中，将读取的内容存储在缓存中，然后一次性的转换成字符串
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //2.读流操作，读到没有为止
        byte[] buffer = new byte[1024];
        //3.记录读取内容的临时变量
        int temp = -1;
        try {
            while ((temp = is.read(buffer)) != -1) {
                bos.write(buffer, 0, temp);
            }
            //返回读取的值
            return bos.toString();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
