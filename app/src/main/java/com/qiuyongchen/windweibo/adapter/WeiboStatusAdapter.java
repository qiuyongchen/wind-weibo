package com.qiuyongchen.windweibo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qiuyongchen.windweibo.R;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;

/**
 * 微博列表适配器
 * Created by qiuyongchen on 2016/2/27.
 */
public class WeiboStatusAdapter extends BaseAdapter {

    private ArrayList<Status> statuses;
    private Context context;

    public WeiboStatusAdapter(Context context, ArrayList<Status> statuses) {
        this.context = context;
        this.statuses = statuses;
    }

    @Override
    public int getCount() {
        return statuses == null ? 0 : statuses.size();
    }

    @Override
    public Object getItem(int position) {
        return statuses == null ? null : statuses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
        // return statuses == null ? 0 : Long.valueOf(statuses.get(position).);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.listview_item_weibo_status, null);
        } else {
            Log.e("WeiboStatusAdapter", String.valueOf(((TextView) view.findViewById(R.id.txt_wb_item_content)).getText()));
        }

        TextView userName = (TextView) view.findViewById(R.id.txt_wb_item_uname);
        TextView statusContent = (TextView) view.findViewById(R.id.txt_wb_item_content);

        Status status = statuses.get(position);

        userName.setText(status.user.screen_name);
        statusContent.setText(status.text);

        Log.e("WeiboStatusAdapter", String.valueOf(((TextView) view.findViewById(R.id.txt_wb_item_uname)).getText()));

        return view;
    }
}
