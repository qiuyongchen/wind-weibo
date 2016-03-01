package com.qiuyongchen.windweibo.imgcache;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.qiuyongchen.windweibo.logic.MyApplication;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 异步缓存图片的主体，含有异步处理的代码
 * Created by qiuyongchen on 2016/3/1.
 */
public class AsyncImgLoader {

    private static final int MSG_SAVE_BITMAP = 1;
    private static final String IMG = "img";
    private static final String IMG_URL = "img_url";

    private ImageManager imageManager;

    private BlockingQueue<String> urls;

    private DownloadImgThread downloadImgThread;

    private CallbackManager callbackManager;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAVE_BITMAP:
                    Bundle bundle = msg.getData();

                    String URL = bundle.getString(IMG_URL);
                    Bitmap bitmap = bundle.getParcelable(IMG);

                    callbackManager.callback(URL, bitmap);
                    break;
            }

        }
    };

    public AsyncImgLoader() {
        imageManager = new ImageManager(MyApplication.context);

        urls = new ArrayBlockingQueue<>(23);

        downloadImgThread = new DownloadImgThread();

        callbackManager = new CallbackManager();
    }

    /**
     * 获取一个URL对应的图片，优先从本地获得，次优先从网络下载得来。
     */
    public Bitmap get(String url, ImageLoaderCallback imgLoaderCallback) {
        Bitmap bitmap = imageManager.getLocal(url);

        // 本地就存在该照片
        if (bitmap != null) {
            return bitmap;
        }

        // 本地不存在该照片，需要从网络上下载
        callbackManager.put(url, imgLoaderCallback);
        startDownloadFromNet(url);

        return null;
    }

    /**
     * 开启线程从网络下载图片
     */
    private void startDownloadFromNet(String url) {
        if (!urls.contains(url)) {
            urls.add(url);
        }

        Thread.State state = downloadImgThread.getState();

        if (state == Thread.State.NEW) {
            downloadImgThread.start();
        } else if (state == Thread.State.TERMINATED) {
            downloadImgThread = new DownloadImgThread();
            downloadImgThread.start();
        }
    }

    /**
     * 下载图片的线程
     */
    private class DownloadImgThread extends Thread {
        private boolean isRunning = true;

        // 使进程停下
        public void stopThread() {
            isRunning = false;
        }

        public void run() {
            try {

                while (isRunning) {
                    String url = urls.poll();

                    if (url == null) {
                        break;
                    }

                    Bitmap bitmap = imageManager.getRemote(url);

                    Message message = handler.obtainMessage(MSG_SAVE_BITMAP);

                    // 利用Bundle来作为载体
                    Bundle bundle = message.getData();
                    // 保存URL
                    bundle.putSerializable(IMG_URL, url);
                    // 保存Bitmap图像
                    bundle.putParcelable(IMG, bitmap);

                    handler.sendMessage(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                // 成功下载图片完后使线程停下
                stopThread();

            }

        }
    }
}
