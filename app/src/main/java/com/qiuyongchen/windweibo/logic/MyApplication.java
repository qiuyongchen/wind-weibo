package com.qiuyongchen.windweibo.logic;

import android.app.Application;
import android.content.Context;

import com.qiuyongchen.windweibo.imgcache.AsyncImgLoader;

/**
 * 系统级的资料，如Context等
 * Created by qiuyongchen on 2016/3/1.
 */
public class MyApplication extends Application {

    // 单例模式：只有一个异步缓存的主体
    public static AsyncImgLoader asyncImgLoader;

    public static Context context;

    @Override
    public void onCreate() {
        context = this.getApplicationContext();

        asyncImgLoader = new AsyncImgLoader();

        super.onCreate();
    }
}
