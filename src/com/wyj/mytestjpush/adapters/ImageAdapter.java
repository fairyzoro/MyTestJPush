package com.wyj.mytestjpush.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.wyj.mytestjpush.R;
import com.wyj.mytestjpush.tools.AsyncDrawable;
import com.wyj.mytestjpush.tools.FileCache;
import com.wyj.mytestjpush.tools.ImageLoadTask;

import java.util.List;

/**
 * Created
 * Author: wyj
 * Email: 18346668711@163.com
 * Date: 2015/10/13
 */
public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;

    public ImageAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(list!=null)
            ret = list.size();

        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.image_item,parent,false);
            ViewHolder holder = new ViewHolder();
            holder.iv = (ImageView)convertView.findViewById(R.id.iv_item);
            convertView.setTag(holder);
        }

        ImageView iv = ((ViewHolder)convertView.getTag()).iv;

        Bitmap bitmap = FileCache.getInstance().getFromCache(list.get(position));
        if(bitmap!=null){
            iv.setImageBitmap(bitmap);
            Log.i("debug",position+"--> url:"+list.get(position)+"从缓存中取出");
        }else{
            Log.i("debug",position+"--> url:"+list.get(position)+"从网络下载");

            ImageLoadTask task = new ImageLoadTask(iv, 256, 128);
            AsyncDrawable drawable = new AsyncDrawable(context.getResources(), null, task);

            //先设置 后执行
            iv.setImageDrawable(drawable);

            if(Build.VERSION.SDK_INT>=11){
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,list.get(position));
            }else {
                task.execute(list.get(position));
            }
        }

        return convertView;
    }


    private static class ViewHolder{
        public ImageView iv;
    }
}
