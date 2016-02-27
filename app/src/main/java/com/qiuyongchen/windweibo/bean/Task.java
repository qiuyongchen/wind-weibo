package com.qiuyongchen.windweibo.bean;

import java.util.Map;

/**
 * 任务
 * Created by qiuyongchen on 2016/2/20.
 */
public class Task {

    // 登录微博
    public static final int LOGIN_WEIBO = 1;
    // 成功授权微博
    public static final int AUTH_WEIBO = 2;
    // 获取当前登录用户及其所关注用户的最新微博
    public static final int FRIENDS_TIMELINE = 3;

    private int taskID;
    private Map<String, Object> taskParams;


    public Task(int taskID, Map<String, Object> taskParams) {
        this.taskID = taskID;
        this.taskParams = taskParams;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Map<String, Object> getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(Map<String, Object> taskParams) {
        this.taskParams = taskParams;
    }
}
