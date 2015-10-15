package com.wyj.mytestjpush.tools;

/**
 * Created
 * Author: wyj
 * Email: 18346668711@163.com
 * Date: 2015/10/12
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import android.util.LruCache;


import java.io.*;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件Cache目录
 * 1、内部存储区cache目录，当手机内部存储满了之后，Android系统会自动清空
 *      cache目录
 * 2、缓存的目录优先使用存储卡进行存储，因为空间大
 * 3、Android系统给应用程序在存储卡上，也创建了一套应用程序私有的目录。
 *      也包含缓存文件夹，cache
 * 4、另外加上 LruCache，SoftReference
 */

public class FileCache {

    //-----------------------------
    // 图片缓存
    private static LruCache<String, Bitmap> lruCache = new LruCache<>(10);

    private static Map<String, SoftReference<Bitmap>> softCache = new HashMap<>();

    /**
     * 将图片 放入缓存
     * @param url
     * @return
     */
    public Bitmap getFromCache(String url) {
        Bitmap ret = null;

        ret = lruCache.get(url);
        if (ret == null) {
            SoftReference<Bitmap> softReference = softCache.get(url);
            if (softReference != null) {
                ret = softReference.get();
                if (ret != null) {
                    lruCache.put(url, ret);
                    softCache.remove(url);
                }
            }
        }

        if (ret == null) {
            ret = loadBitmap(url);
            if (ret != null)
                lruCache.put(url, ret);
            }
        return ret;
    }

    /**
     * 将图片从缓存中取出
     * @param url
     * @param bitmap
     */
    public void putInCache(String url, Bitmap bitmap){
        lruCache.put(url,bitmap);
        saveBitmap(url,bitmap,FileCache.FORMAT_PNG);
    }


    //-----------------------------

    /**
     * PNG格式的图片
     */
    public static final int FORMAT_PNG = 1;
    /**
     * JPEG格式的图片
     */
    public static final int FORMAT_JPEG = 2;

    private Context context;

    public static FileCache newInstance(Context context){

        if(context!=null){
            if (ourInstance == null) {
                ourInstance = new FileCache(context);
            }

        }else{
            throw new IllegalArgumentException("context can't be null");
        }
        return ourInstance;
    }

    public static FileCache getInstance(){
        if (ourInstance == null) {
            throw new IllegalArgumentException("newInstance invoke");
        }
        return ourInstance;
    }

    private static FileCache ourInstance;

    private FileCache(Context context) {
        this.context = context;
    }


    /**
     * 从文件存储加载对应网址的内容
     * @param url
     * @return
     */
    public byte[] load(String url){
        //TODO 通过网址找文件
        byte[] ret = null;

        if(url!=null) {
            //1. 最终获取出来的文件缓存目录
            File cacheDir = null;

            if (isMounted()) {
                //可以获取存储卡中，应用程序私有的缓存目录
                cacheDir = context.getExternalCacheDir();
            } else {
                //获取手机内部存储中，应用程序的缓存目录
                cacheDir = context.getCacheDir();
            }

            //2. 映射文件名称
            String fileName = EncryptUtils.md5(url);
            File targetFile = new File(cacheDir, fileName);

            Log.i("debug","load url-->"+url);
            Log.i("debug","load fileName-->"+targetFile.getAbsolutePath());

            if(targetFile.exists()){
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(targetFile);
                    ret = StreamUtil.readStream(fis);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    StreamUtil.close(fis);
                }

            }
        }

        return ret;
    }

    /**
     * 保存对应网址的数据，到文件中
     * @param url
     * @param data
     */
    public void save(String url, byte[] data) {
        //TODO 通过网址存文件

        if(url!=null && data!=null) {
            //1. 最终获取出来的文件缓存目录
            File cacheDir = null;

            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                //可以获取存储卡中，应用程序私有的缓存目录
                cacheDir = context.getExternalCacheDir();
            } else {
                //获取手机内部存储中，应用程序的缓存目录
                cacheDir = context.getCacheDir();
            }

            //2. 映射文件名称
            String fileName = EncryptUtils.md5(url);
            File targetFile = new File(cacheDir,fileName);

            //3. IO操作，保存文件
            FileOutputStream fout = null;

            try {
                fout = new FileOutputStream(targetFile);
                fout.write(data);
                fout.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                StreamUtil.close(fout);

            }

        }
    }


    /**
     * 从文件存储 加载对应url的图片
     * @param url
     * @return
     */
    public Bitmap loadBitmap(String url){
        Bitmap ret = null;

        byte[] data = load(url);
        if(data!=null){
            ret = BitmapFactory.decodeByteArray(data, 0, data.length);
        }

        return ret;
    }

    /**
     * 保存对应网址的图片到文件系统中
     * @param url
     * @param bitmap
     * @param format 以哪种形式保存图片 FORMAT_PNG or FORMAT_JPEG
     */
    public void saveBitmap(String url, Bitmap bitmap, int format){
        File cacheDir = null;

        if(isMounted()){
            //获取应用程序在sdcard上的私有存储目录
            cacheDir = Environment.getExternalStorageDirectory();
        }else{
            //获取应用程序在手机存储上的私有cache目录
            cacheDir = context.getCacheDir();
        }

        File file = new File(cacheDir, EncryptUtils.md5(url));

        Log.i("debug","load url-->"+url);
        Log.i("debug","load fileName-->"+file.getAbsolutePath());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            //默认以PNG保存
            bitmap.compress(format==FORMAT_JPEG? Bitmap.CompressFormat.JPEG: Bitmap.CompressFormat.PNG,
                    50,fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断外部存储卡是否挂载
     * @return
     */
    public static boolean isMounted(){
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 存储卡的或手机存储的剩余空间
     * @return 若存储卡挂载，返回剩余存储卡的空间；若存储卡没有挂载，返回手机存储的剩余空间  单位：M
     */
    public double getAble(){
        double ret = -1;
        StatFs fs = null;
        if (isMounted()){
            fs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
        }else {
            fs = new StatFs(context.getCacheDir().getAbsolutePath());
        }
        long size = fs.getAvailableBytes();
        ret = size/1024.0/1024.0;//单位M

        return ret;
    }

    public void clearCash(){
        File cacheDir = null;
        if(isMounted()){
            cacheDir = Environment.getExternalStorageDirectory();
        }else{
            cacheDir = context.getCacheDir();
        }

        File[] arr = cacheDir.listFiles();
        for(File f:arr){
            f.delete();
        }
    }

}
