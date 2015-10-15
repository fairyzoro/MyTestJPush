package com.wyj.mytestjpush.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created
 * Author: wyj
 * Email: 18346668711@163.com
 * Date: 2015/10/14
 */
public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {

    //user WeakReference
    private final WeakReference<ImageView> weakReference;

    private int requestWidth,requestHeight;

    public ImageLoadTask(ImageView imageView, int requestWidth, int requestHeight){
        this.weakReference = new WeakReference<ImageView>(imageView);
        this.requestWidth = requestWidth;
        this.requestHeight = requestHeight;
    }


    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap ret = null;

        if(params!=null && params.length>0){
            String url = params[0];
            Log.i("debug", "下载--> url:" + url);

            byte[] data = HttpTools.doGet(url);
            if(data!=null) {
                ret = twiceGetBitmap(data, 256, 128);
                //放入缓存
                FileCache.getInstance().putInCache(url,ret);
                Log.i("debug", "保存到缓存--> url:" + url);
            }

        }

        return ret;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            ImageView imageView = weakReference.get();
            if(imageView!=null){
                Drawable drawable = imageView.getDrawable();
                if(drawable!=null && drawable instanceof AsyncDrawable){
                    //用 检测图片错位的情况
                    AsyncDrawable asyncDrawable = (AsyncDrawable)drawable;
                    ImageLoadTask task = asyncDrawable.getImageLoaderTask();
                    if(task==this){
                        imageView.setImageBitmap(bitmap);
                    }
                }else{
                    //不用 检测图片的错位情况
                    imageView.setImageBitmap(bitmap);
                }
            }
            imageView.setImageBitmap(bitmap);
        }
    }


    //get bitmap use twice BitmapFactory.decodeBitmap
    public static Bitmap twiceGetBitmap(byte[] data, int reqW, int reqH){
        Bitmap ret = null;

        if(data!=null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeByteArray(data,0,data.length,options);

            options.inSampleSize = calculateInSampleSize(options,reqW,reqH);

            options.inJustDecodeBounds = false;

            options.inPreferredConfig = Bitmap.Config.RGB_565;

            ret = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        }

        return ret;
    }


    /**
     * 计算图片二次采样的采样率，使用获取图片宽高之后的 Options 作为第一个参数
     *  并且，通过请求的 宽度、高度尺寸，进行采样率的计算
     * @param options
     * @param reqWidth 请求的宽度
     * @param reqHeight 请求的高度
     * @return int 采样率
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        //当请求的宽度、高度 >0 的时候，进行缩放
        //否则 图片不进行缩放
        if(reqWidth>0 && reqWidth>0) {
            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }
        }

        return inSampleSize;
    }

}
