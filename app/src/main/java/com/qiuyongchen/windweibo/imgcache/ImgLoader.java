package com.qiuyongchen.windweibo.imgcache;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.qiuyongchen.windweibo.logic.MyApplication;

import java.util.Objects;

/**
 * 将一个URL对应的图片赋给一个ImageView
 * Created by qiuyongchen on 2016/3/1.
 */
public class ImgLoader {

    public static void show(ImageView view, String url) {
        view.setTag(url);

        // 将URL和回调接口传给异步缓存主体
        view.setImageBitmap(MyApplication.asyncImgLoader.get(url, getCallback(view, url)));
    }

    private static ImgLoaderCallback getCallback(final ImageView view, String url) {
        return new ImgLoaderCallback() {
            @Override
            public void refresh(String url, Bitmap bitmap) {
                if (Objects.equals(view.getTag().toString(), url)) {
                    view.setImageBitmap(bitmap);
                }
            }
        };
    }
}
