package com.wyj.mytestjpush;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import com.wyj.mytestjpush.tools.FileCache;

/**
 * Created
 * Author: wyj
 * Email: 18346668711@163.com
 * Date: 2015/10/14
 */
public class MainApplication extends Application {
    private static final String TAG = "JPush";


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化 FileCache 工具类
        FileCache.newInstance(this);

        //初始化 极光推送 工具类
        Log.d(TAG, "[ExampleApplication] onCreate");
        super.onCreate();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

    }
}
