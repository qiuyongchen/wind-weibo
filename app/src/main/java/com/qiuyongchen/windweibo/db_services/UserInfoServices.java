package com.qiuyongchen.windweibo.db_services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.qiuyongchen.windweibo.bean.UserInfo;
import com.qiuyongchen.windweibo.db.DBHelper;
import com.qiuyongchen.windweibo.db.DBinfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 与数据库交互，负责与用户信息相关的操作
 * Created by qiuyongchen on 2016/2/22.
 */
public class UserInfoServices {

    private DBHelper dbHelper;

    public UserInfoServices(Context context) {
        dbHelper = new DBHelper(context);
    }

    // 将一个用户的信息保存到数据库中
    public void insertUser(UserInfo userInfo) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(6);

        contentValues.put(UserInfo.UID, userInfo.getUid());
        contentValues.put(UserInfo.USER_NAME, userInfo.getUser_name());
        contentValues.put(UserInfo.ACCESS_TOKEN, userInfo.getAccess_token());
        contentValues.put(UserInfo.EXPIRES_IN, userInfo.getExpires_in());
        contentValues.put(UserInfo.REFRESH_TOKEN, userInfo.getRefresh_token());
        contentValues.put(UserInfo.IS_DEFAULT, userInfo.getIsDefault());

        sqLiteDatabase.insert(DBinfo.TABLE.USER_INFO_TB_NAME, null, contentValues);

        sqLiteDatabase.close();
    }

    // 查询某个用户的信息
    public UserInfo getUser(String uid) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DBinfo.TABLE.USER_INFO_TB_NAME, new String[]
                        {UserInfo.ID, UserInfo.UID, UserInfo.USER_NAME, UserInfo.ACCESS_TOKEN,
                                UserInfo.EXPIRES_IN, UserInfo.REFRESH_TOKEN, UserInfo.IS_DEFAULT},
                UserInfo.UID + " = ?", new String[]{uid}, null, null, null);

        UserInfo userInfo = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                userInfo = new UserInfo(
                        cursor.getLong(cursor.getColumnIndex(UserInfo.ID)),
                        cursor.getString(cursor.getColumnIndex(UserInfo.UID)),
                        cursor.getString(cursor.getColumnIndex(UserInfo.USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(UserInfo.ACCESS_TOKEN)),
                        cursor.getString(cursor.getColumnIndex(UserInfo.EXPIRES_IN)),
                        cursor.getString(cursor.getColumnIndex(UserInfo.REFRESH_TOKEN)),
                        cursor.getString(cursor.getColumnIndex(UserInfo.IS_DEFAULT)));
            }

            cursor.close();
        }

        sqLiteDatabase.close();

        return userInfo;
    }

    // 数据库是否为空
    public boolean isEmpty() {
        boolean empty = true;

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DBinfo.TABLE.USER_INFO_TB_NAME, new String[]
                {UserInfo.ID, UserInfo.UID, UserInfo.USER_NAME, UserInfo.ACCESS_TOKEN,
                        UserInfo.EXPIRES_IN, UserInfo.REFRESH_TOKEN,
                        UserInfo.IS_DEFAULT, UserInfo.USER_ICON}, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                empty = false;
            }

            cursor.close();
        }

        sqLiteDatabase.close();

        return empty;
    }

    // 查看数据库中是否存在某个用户
    public boolean exists(UserInfo userInfo) {
        boolean exists = false;

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DBinfo.TABLE.USER_INFO_TB_NAME, new String[]
                        {UserInfo.ID, UserInfo.UID, UserInfo.USER_NAME, UserInfo.ACCESS_TOKEN,
                                UserInfo.EXPIRES_IN, UserInfo.REFRESH_TOKEN, UserInfo.IS_DEFAULT},
                UserInfo.UID + " = ?", new String[]{userInfo.getUid()}, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                exists = true;
            }

            cursor.close();
        }

        sqLiteDatabase.close();

        return exists;

    }

    // 获取所有用户信息
    public List<UserInfo> getAllUser() {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        List<UserInfo> users = null;

        Cursor cursor = sqLiteDatabase.query(DBinfo.TABLE.USER_INFO_TB_NAME, new String[]
                {UserInfo.ID, UserInfo.UID, UserInfo.USER_NAME, UserInfo.ACCESS_TOKEN,
                        UserInfo.EXPIRES_IN, UserInfo.REFRESH_TOKEN,
                        UserInfo.IS_DEFAULT, UserInfo.USER_ICON}, null, null, null, null, null);

        UserInfo userInfo = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                users = new ArrayList<>(cursor.getCount());

                while (cursor.moveToNext()) {
                    userInfo = new UserInfo(
                            cursor.getLong(cursor.getColumnIndex(UserInfo.ID)),
                            cursor.getString(cursor.getColumnIndex(UserInfo.UID)),
                            cursor.getString(cursor.getColumnIndex(UserInfo.USER_NAME)),
                            cursor.getString(cursor.getColumnIndex(UserInfo.ACCESS_TOKEN)),
                            cursor.getString(cursor.getColumnIndex(UserInfo.EXPIRES_IN)),
                            cursor.getString(cursor.getColumnIndex(UserInfo.REFRESH_TOKEN)),
                            cursor.getString(cursor.getColumnIndex(UserInfo.IS_DEFAULT)));

                    // 从数据库中获得二进制文件
                    byte[] icon = cursor.getBlob(cursor.getColumnIndex(UserInfo.USER_ICON));

                    if (icon != null) {
                        // 将二进制文件转换为二进制数组流
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(icon);
                        // 从二进制数组流中创建Drawable对象
                        Drawable drawable = Drawable.createFromStream(byteArrayInputStream, "img");
                        userInfo.setUser_icon(drawable);
                    }

                    users.add(userInfo);
                }
            }

            cursor.close();
        }

        sqLiteDatabase.close();

        return users;
    }

    // 更新某个用户的昵称和头像
    public void updateUser(String uid, String user_name, Bitmap user_icon) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        user_icon.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues(2);
        contentValues.put(UserInfo.USER_NAME, user_name);
        contentValues.put(UserInfo.USER_ICON, byteArrayOutputStream.toByteArray());

        sqLiteDatabase.update(DBinfo.TABLE.USER_INFO_TB_NAME, contentValues,
                UserInfo.UID + " = ? ", new String[]{uid});

        sqLiteDatabase.close();
    }
}
