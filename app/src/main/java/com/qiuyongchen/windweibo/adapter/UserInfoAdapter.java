package com.qiuyongchen.windweibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiuyongchen.windweibo.R;
import com.qiuyongchen.windweibo.bean.UserInfo;

import java.util.List;

/**
 * Created by qiuyongchen on 2016/2/25.
 */
public class UserInfoAdapter extends BaseAdapter {
    private Context context;
    private List<UserInfo> userInfos;

    public UserInfoAdapter(Context context, List<UserInfo> userInfos) {
        this.context = context;
        this.userInfos = userInfos;
    }

    @Override
    public int getCount() {
        return userInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return userInfos == null ? null : userInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (userInfos != null) return userInfos.get(position).get_id();
        else return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_users, null);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView_user_head);
            TextView textView = (TextView) view.findViewById(R.id.tv_user_name);

            UserInfo userInfo = userInfos.get(position);
            textView.setText(userInfo.getUser_name());
            imageView.setImageDrawable(userInfo.getUser_icon());
        }

        return view;
    }
}
