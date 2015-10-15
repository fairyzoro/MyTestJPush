package com.wyj.mytestjpush.tools;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * Created
 * Author: wyj
 * Email: 18346668711@163.com
 * Date: 2015/10/12
 */
public final class StreamUtil {
    private StreamUtil(){}

    /**
     * 读流
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readStream(InputStream in) throws IOException {
        byte[] ret = null;

        if (in != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = -1;
            while((len=in.read(buffer))!=-1){
                baos.write(buffer,0,len);
            }

            //注意 buffer 必须要进行 =null的操作
            //减少内存溢出的可能性
            //（图片太多的话，buffer又回收不及时）就会占用很多的内存
            buffer = null;

            ret = baos.toByteArray();

            baos.close();
        }

        return ret;
    }

    /**
     * 关闭各种IO流，以及HttpURLConnection
     * @param streams
     */
    public static void close(Object... streams){

        if(streams != null) {
            for (Object stream : streams) {

                try {
                    if (stream instanceof InputStream) {
                        ((InputStream) stream).close();
                    } else if (stream instanceof OutputStream) {
                        ((OutputStream) stream).close();
                    } else if (stream instanceof Reader) {
                        ((Reader) stream).close();
                    } else if (stream instanceof Writer) {
                        ((Writer) stream).close();
                    } else if (stream instanceof HttpURLConnection) {
                        ((HttpURLConnection) stream).disconnect();
                    }
                } catch (Exception e) {

                }

            }
        }
    }

}
