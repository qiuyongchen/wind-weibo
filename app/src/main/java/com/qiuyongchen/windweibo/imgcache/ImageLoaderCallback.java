package com.qiuyongchen.windweibo.imgcache;

import android.graphics.Bitmap;

/**
 * 异步缓存图片之后的回调接口（方便逻辑下层的代码刷新逻辑上层的ImageView）
 * Created by qiuyongchen on 2016/3/1.
 */
public interface ImageLoaderCallback {

    // 加载照片的回调
    void refresh(String url, Bitmap bitmap);
}