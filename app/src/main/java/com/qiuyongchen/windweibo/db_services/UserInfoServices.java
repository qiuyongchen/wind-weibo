package com.qiuyongchen.windweibo.db_services;

import android.content.Context;

import com.qiuyongchen.windweibo.bean.UserInfo;
import com.qiuyongchen.windweibo.db.DBHelper;

/**
 * 与数据库交互，负责与用户信息相关的操作
 * Created by qiuyongchen on 2016/2/22.
 */
public class UserInfoServices {

    private DBHelper dbHelper;

    public UserInfoServices(Context context) {
        dbHelper = new DBHelper(context);
    }

    private void insertUser(UserInfo userInfo) {

    }

}
