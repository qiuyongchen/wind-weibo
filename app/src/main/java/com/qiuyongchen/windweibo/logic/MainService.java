package com.qiuyongchen.windweibo.logic;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.qiuyongchen.windweibo.UI.BaseActivity;
import com.qiuyongchen.windweibo.Util.NetUtil;
import com.qiuyongchen.windweibo.bean.Task;
import com.qiuyongchen.windweibo.db_services.UserInfoServices;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static com.qiuyongchen.windweibo.weibo.AccessTokenKeeper.readAccessToken;

public class MainService extends Service implements Runnable {

    // 用户授权信息（全局资料）
    public static Oauth2AccessToken accessToken;
    // 用户昵称地址等信息（全局资料）
    public static User user;


    // 任务队列
    private static Queue<Task> tasks = new LinkedList<>();
    // UI集合
    private static ArrayList<Activity> activities = new ArrayList<>();
    // 在UI线程中处理任务
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case Task.LOGIN_WEIBO:
                    break;

                case Task.AUTH_WEIBO:

                    BaseActivity loginActivity = (BaseActivity) getActivity("LoginActivity");
                    if (loginActivity != null) {
                        loginActivity.refresh();
                    }

                    break;
            }
        }
    };

    // 任务是否在执行
    private boolean isRunning;

    /**
     * 把任务添加到队列中去
     *
     * @param task 待添加的任务
     */
    public static void newTask(Task task) {
        tasks.add(task);
    }

    /**
     * 把Activity加到集合中
     *
     * @param activity 待添加的UI
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    @Override
    public void onCreate() {

        isRunning = true;

        accessToken = readAccessToken(getApplicationContext());

        // Mainservice一开启就把自身加入线程，在UI线程之外独立运行
        Thread thread = new Thread(this);
        thread.start();

        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * MainService一直在运行，不断地监听任务
     */
    @Override
    public void run() {

        //
        while (isRunning) {
            Task task = null;

            if (!tasks.isEmpty()) {
                task = tasks.poll();
            }

            if (task != null) {
                doTask(task);
            }

            try {
                // 让MainService睡眠，免得过于频繁导致系统卡塞
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 在Mainservice中处理任务，将结果传到UI线程
     *
     * @param task 待处理的任务
     */
    private void doTask(Task task) {
        Message message = handler.obtainMessage();
        message.what = task.getTaskID();

        switch (task.getTaskID()) {
            case Task.LOGIN_WEIBO:
                Log.e("MainService", "doTask LOGIN_WEIBO");

                accessToken = (Oauth2AccessToken) task.getTaskParams().get("accessToken");

                message.obj = "doTask LOGIN_WEIBO";
                break;

            // 得到授权信息后，进一步获取用户昵称和头像等信息
            case Task.AUTH_WEIBO:
                Log.e("MainService", "doTask AUTH_WEIBO");

                user = (User) task.getTaskParams().get("User");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            try {
                                // 获取头像地址
                                URL url = new URL(user.profile_image_url);

                                // 将Drawable转为Bitmap
                                BitmapDrawable bitmapDrawable = (BitmapDrawable) NetUtil.getDrawable(url);

                                UserInfoServices userInfoServices = new UserInfoServices(getApplicationContext());

                                // 更新数据库中用户的昵称和头像
                                userInfoServices.updateUser(user.id, user.screen_name, bitmapDrawable.getBitmap());

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
                break;

            // 得到授权信息后，进一步获取用户昵称和头像等信息
            case Task.FRIENDS_TIMELINE:
                Log.e("MainService", "doTask FRIENDS_TIMELINE");


                break;
        }

        handler.sendMessage(message);
    }

    /**
     * 根据UI的名字，在UI集合中找到该UI
     *
     * @param name UI的名字
     * @return UI实例
     */
    private Activity getActivity(String name) {
        if (!activities.isEmpty()) {
            for (Activity activity : activities) {
                if (activity != null) {
                    if (activity.getClass().getName().contains(name)) {
                        return activity;
                    }
                }
            }
        }

        return null;
    }
}
