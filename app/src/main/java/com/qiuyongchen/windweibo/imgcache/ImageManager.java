package com.qiuyongchen.windweibo.imgcache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.qiuyongchen.windweibo.Util.MD5Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 负责从本地或网络上取来一个URL对应的图片
 * Created by qiuyongchen on 2016/3/1.
 */
public class ImageManager {
    private Map<String, SoftReference<Bitmap>> imgCacheMap;
    private Context context;

    public ImageManager(Context context) {
        imgCacheMap = new HashMap<>();
        this.context = context;
    }

    /**
     * 优先从缓存中取出该照片，次优先从内存卡上取出
     */
    public Bitmap getLocal(String url) {
        Bitmap bitmap = getFromCache(url);

        if (bitmap == null) {
            bitmap = getFromSDCard(url);
        }

        return bitmap;
    }

    /**
     * 优先从内存卡上取照片，次优先从网络上取
     */
    public Bitmap getRemote(String url) {
        Bitmap bitmap = getFromSDCard(url);

        if (bitmap != null) {
            imgCacheMap.put(url, new SoftReference<>(bitmap));
            return bitmap;
        }

        return getFromNet(url);
    }

    /**
     * 从缓存取出URL对应的图片
     */
    private Bitmap getFromCache(String url) {
        Bitmap bitmap = null;
        SoftReference<Bitmap> softReference;

        synchronized (this) {
            softReference = imgCacheMap.get(url);
        }

        if (softReference != null) {
            bitmap = softReference.get();
        }

        return bitmap;
    }

    /**
     * 从内存卡上取出URL对应的图片
     */
    private Bitmap getFromSDCard(String url) {
        String fileName = getMD5(url);

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(fileName);

            return BitmapFactory.decodeStream(fileInputStream);
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }

    /**
     * 返回MD5编码
     */
    private String getMD5(String url) {
        return MD5Util.GetMD5Code(url);
    }

    /**
     * 从URL下载图片，并保存到内存卡上
     */
    private Bitmap getFromNet(String urlStr) {

        Bitmap bitmap = null;

        if (urlStr != null) {
            try {
                URL url = new URL(urlStr);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                // 将获得的网络流保存为文件
                String fileName = WriteToFile(MD5Util.GetMD5Code(urlStr), httpURLConnection.getInputStream());

                bitmap = BitmapFactory.decodeFile(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;
    }

    /**
     * 将流保存为文件
     */
    private String WriteToFile(String fileName, InputStream inputStream) {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));

            // 利用缓存的方式来读写
            byte[] buffer = new byte[4096];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭输入输出流
            if (bufferedInputStream != null)
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            if (bufferedOutputStream != null)
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return context.getFilesDir() + "/" + fileName;

    }
}
