package com.wyj.mytestjpush;

import android.app.Application;
import com.wyj.mytestjpush.tools.FileCache;

/**
 * Created
 * Author: wyj
 * Email: 18346668711@163.com
 * Date: 2015/10/14
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化 FileCache 工具类
        FileCache.newInstance(this);

        //初始化 极光推送 工具类

    }
}
