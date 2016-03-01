package com.qiuyongchen.windweibo.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qiuyongchen.windweibo.R;
import com.qiuyongchen.windweibo.imgcache.ImgLoader;
import com.sina.weibo.sdk.openapi.models.Status;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 微博列表适配器
 * Created by qiuyongchen on 2016/2/27.
 */
public class WeiboStatusAdapter extends BaseAdapter {

    private ArrayList<Status> statuses;
    private Context context;
    private LayoutInflater layoutInflater;

    public WeiboStatusAdapter(Context context, ArrayList<Status> statuses) {
        this.context = context;
        this.statuses = statuses;
        layoutInflater = LayoutInflater.from(context);
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
        StatusHolder statusHolder;
        Status status;

        if (view == null) {
            statusHolder = new StatusHolder();

            view = layoutInflater.inflate(R.layout.listview_item_weibo_status, null);

            convertView = view;

            convertView.setTag(statusHolder);
        } else {
            statusHolder = (StatusHolder) convertView.getTag();
            Log.e("WeiboStatusAdapter", String.valueOf(((TextView) view.findViewById(R.id.txt_wb_item_content)).getText()));
        }

        status = statuses.get(position);

        // TextView userName = (TextView) view.findViewById(R.id.txt_wb_item_uname);
        // TextView statusContent = (TextView) view.findViewById(R.id.txt_wb_item_content);

        statusHolder.textViewUserScreenName = (TextView) view.findViewById(R.id.txt_wb_item_uname);
        statusHolder.imageViewUserHead = (ImageView) view.findViewById(R.id.img_wb_item_head);
        statusHolder.textViewStatusContent = (TextView) view.findViewById(R.id.txt_wb_item_content);
        statusHolder.textViewStatusFrom = (TextView) view.findViewById(R.id.txt_wb_item_from);
        statusHolder.textViewStatusTime = (TextView) view.findViewById(R.id.txt_wb_item_time);

        statusHolder.textViewStatusFrom.setMovementMethod(LinkMovementMethod.getInstance());
        statusHolder.textViewStatusRedirect = (TextView) view.findViewById(R.id.txt_wb_item_redirect);
        statusHolder.textViewStatusCommentNum = (TextView) view.findViewById(R.id.txt_wb_item_comment);

        // 昵称
        statusHolder.textViewUserScreenName.setText(status.user.screen_name);
        // 用户头像
        ImgLoader.show(statusHolder.imageViewUserHead, status.user.avatar_large);
        // 微博正文
        statusHolder.textViewStatusContent.setText(status.text);
        // 微博发布时间
        statusHolder.textViewStatusTime.setText(status.created_at.substring(11, 20));
        // 微博来源
        statusHolder.textViewStatusFrom.setText(String.format("%s", Html.fromHtml(status.source)));
        // 转发数量
        statusHolder.textViewStatusRedirect.setText(String.valueOf(status.reposts_count));
        // 评论数量
        statusHolder.textViewStatusCommentNum.setText(String.valueOf(status.comments_count));

        // 通过认证
        if (status.user.verified) {
            statusHolder.imageViewUserV = (ImageView) view.findViewById(R.id.img_wb_item_V);
            statusHolder.imageViewUserV.setVisibility(View.VISIBLE);
        }

        // 正文内容中有照片
        if (!(status.thumbnail_pic == null || Objects.equals(status.thumbnail_pic, ""))) {
            statusHolder.imageViewStatusContentPic = (ImageView) view.findViewById(R.id.img_wb_item_content_pic);
            statusHolder.imageViewStatusContentPic.setVisibility(View.VISIBLE);
            ImgLoader.show(statusHolder.imageViewStatusContentPic, status.bmiddle_pic);
        }

        // 转发内容
        if (status.retweeted_status != null) {
            statusHolder.linearLayoutStatusRetweeted = (LinearLayout) view.findViewById(R.id.layout_retweeted);
            statusHolder.linearLayoutStatusRetweeted.setVisibility(View.VISIBLE);

            statusHolder.textViewStatusRetweetedContent =
                    (TextView) view.findViewById(R.id.txt_wb_item_subcontent);
            statusHolder.textViewStatusRetweetedContent.setText(status.retweeted_status.text);

            // 转发内容中有照片
            if (!(status.retweeted_status.thumbnail_pic == null ||
                    Objects.equals(status.retweeted_status.thumbnail_pic, ""))) {
                statusHolder.imageViewStatusRetweetedPic =
                        (ImageView) view.findViewById(R.id.img_wb_item_content_subpic);
                statusHolder.imageViewStatusRetweetedPic.setVisibility(View.VISIBLE);
                ImgLoader.show(statusHolder.imageViewStatusRetweetedPic, status.retweeted_status.pic_urls.get(0));
            }
        }

        //
        // userName.setText(status.user.screen_name);
        // statusContent.setText(status.text);

        Log.e("WeiboStatusAdapter", String.valueOf(((TextView) view.findViewById(R.id.txt_wb_item_uname)).getText()));

        return view;
    }

    private static class StatusHolder {
        // 发微博者的头像
        ImageView imageViewUserHead;
        // 发微博者的昵称
        TextView textViewUserScreenName;
        // 发微博者是否是大V
        ImageView imageViewUserV;
        // 微博发出时间
        TextView textViewStatusTime;
        // 微博正文
        TextView textViewStatusContent;
        // 微博正文中的图片
        ImageView imageViewStatusContentPic;
        // 微博转发模块的线性布局
        LinearLayout linearLayoutStatusRetweeted;
        // 微博转发模块的内容
        TextView textViewStatusRetweetedContent;
        // 微博转发模块的图片
        ImageView imageViewStatusRetweetedPic;
        // 微博来源
        TextView textViewStatusFrom;
        // 转发微博
        TextView textViewStatusRedirect;
        // 微博评论数量
        TextView textViewStatusCommentNum;
    }
}
