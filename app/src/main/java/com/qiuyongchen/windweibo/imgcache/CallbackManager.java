package com.qiuyongchen.windweibo.imgcache;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理所有的回调
 * Created by qiuyongchen on 2016/3/1.
 */
public class CallbackManager {

    private ConcurrentHashMap<String, List<ImgLoaderCallback>> callbackMap;

    public CallbackManager() {
        callbackMap = new ConcurrentHashMap<>();
    }

    public void callback(String url, Bitmap bitmap) {
        List<ImgLoaderCallback> callbacks = callbackMap.get(url);

        if (callbacks == null) {
            return;
        }

        for (ImgLoaderCallback callback : callbacks) {
            callback.refresh(url, bitmap);
        }

        callbacks.clear();
        callbackMap.remove(url);
    }

    public void put(String url, ImgLoaderCallback imgLoaderCallback) {
        if (!callbackMap.containsKey(url)) {
            callbackMap.put(url, new ArrayList<ImgLoaderCallback>());
        }

        callbackMap.get(url).add(imgLoaderCallback);
    }
}
