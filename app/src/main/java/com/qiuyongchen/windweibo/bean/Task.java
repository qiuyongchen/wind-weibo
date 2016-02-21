package com.qiuyongchen.windweibo.bean;

import java.util.Map;
import java.util.Objects;

/**
 * 任务
 * Created by qiuyongchen on 2016/2/20.
 */
public class Task {

    // 登录微博
    public static final int LOGIN_WEIBO = 1;
    private int taskID;
    private Map<String, Objects> taskParams;


    public Task(int taskID, Map<String, Objects> taskParams) {
        this.taskID = taskID;
        this.taskParams = taskParams;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public Map<String, Objects> getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(Map<String, Objects> taskParams) {
        this.taskParams = taskParams;
    }
}
