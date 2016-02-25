package com.qiuyongchen.windweibo.bean;

import android.graphics.drawable.Drawable;

/**
 * 用户信息的实体
 * Created by qiuyongchen on 2016/2/22.
 */
public class UserInfo {
    public static String ID = "_id";
    public static String UID = "uid";
    public static String USER_NAME = "user_name";
    public static String ACCESS_TOKEN = "access_token";
    public static String EXPIRES_IN = "expires_in";
    public static String REFRESH_TOKEN = "refresh_token";
    public static String IS_DEFAULT = "isDefault";
    public static String USER_ICON = "user_icon";
    private long _id;
    private String uid;
    private String user_name;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String isDefault;
    private Drawable user_icon;

    public UserInfo(String uid, String user_name, String access_token, String expires_in, String refresh_token, String isDefault) {
        this.uid = uid;
        this.user_name = user_name;
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.isDefault = isDefault;
    }

    public UserInfo(long _id, String uid, String user_name, String access_token, String expires_in, String refresh_token, String isDefault) {
        this._id = _id;
        this.uid = uid;
        this.user_name = user_name;
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.isDefault = isDefault;
    }

    public UserInfo(long _id, String uid, String user_name, String access_token, String expires_in, String refresh_token, String isDefault, Drawable user_icon) {
        this._id = _id;
        this.uid = uid;
        this.user_name = user_name;
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.isDefault = isDefault;
        this.user_icon = user_icon;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Drawable getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(Drawable user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
