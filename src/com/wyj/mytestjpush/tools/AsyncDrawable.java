package com.wyj.mytestjpush.tools;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created
 * Author: wyj
 * Email: 18346668711@163.com
 * Date: 2015/10/14
 */
public class AsyncDrawable extends BitmapDrawable {

    private final WeakReference<ImageLoadTask> taskReference;

    public AsyncDrawable(Resources resources, Bitmap bitmap, ImageLoadTask imageLoadTask){
        super(resources,bitmap);
        this.taskReference = new WeakReference<ImageLoadTask>(imageLoadTask);
    }

    public ImageLoadTask getImageLoaderTask(){
        return taskReference.get();
    }

}
