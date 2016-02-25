package com.qiuyongchen.windweibo.Util;

import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by qiuyongchen on 2016/2/25.
 */
public class NetUtil {

    /**
     * 获取URL所表示的Drawable对象
     *
     * @param url 图像的URL
     * @return Drawable对象
     */
    public static Drawable getDrawable(URL url) {
        if (url != null) {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                return Drawable.createFromStream(httpURLConnection.getInputStream(), "image");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
