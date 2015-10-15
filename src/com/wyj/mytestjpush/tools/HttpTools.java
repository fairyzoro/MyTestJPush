package com.wyj.mytestjpush.tools;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created
 * Author: wyj
 * Email: 18346668711@163.com
 * Date: 2015/10/12
 */

//工具类 不能继承 所以final，不能new 所以private Constructor
public final class HttpTools {

    private HttpTools(){}

    public static byte[] doGet(String url){
        byte[] ret = null;

        //先判断参数 在重写内容
        if (url != null) {
            HttpURLConnection conn = null;
            try {
                URL u = new URL(url);
                conn = (HttpURLConnection) u.openConnection();

                conn.connect();

                if(conn.getResponseCode()==200){
                    //TODO 给 data 赋值
                    InputStream fis = null;

                    try {
                        fis = conn.getInputStream();
                        ret = StreamUtil.readStream(fis);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        StreamUtil.close(fis);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                StreamUtil.close(conn);
            }
        }

        return ret;
    }

    public static byte[] doPost(String url, Map<String,String> values){
        byte[] ret = null;

        if(url!=null){
            //把参数拼接成 xx=xx&xx=xx的形式
            StringBuilder sb = new StringBuilder();
            if(values!=null && !values.isEmpty()){
                for(Map.Entry<String,String> en:values.entrySet()){
                    sb.append(en.getKey()).append("=").append(en.getValue()).append("&");
                }
                if(sb.length()>0)
                    sb.deleteCharAt(sb.length()-1);
            }

            HttpURLConnection conn = null;
            try {
                URL u = new URL(url);
                conn = (HttpURLConnection) u.openConnection();

                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POSE");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                //设置提交数据类型
                conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

                //设置提交数据长度
                byte[] data = sb.toString().getBytes("utf-8");
                conn.setRequestProperty("Content-Length", new String(data));

                OutputStream os = conn.getOutputStream();
                os.write(data);
                os.flush();
                os.close();

                if(conn.getResponseCode()==200) {
                    ret = StreamUtil.readStream(conn.getInputStream());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                StreamUtil.close(conn);
            }
        }


        return ret;
    }

}
