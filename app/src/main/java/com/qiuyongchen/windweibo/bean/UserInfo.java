package com.qiuyongchen.windweibo.bean;

import android.graphics.drawable.Drawable;

/**
 * 用户信息的实体
 * Created by qiuyongchen on 2016/2/22.
 */
public class UserInfo {
    private int _id;
    private String uid;
    private String user_name;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String isDefault;
    private Drawable user_icon;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
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
