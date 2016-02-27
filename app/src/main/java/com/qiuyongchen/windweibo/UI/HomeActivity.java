package com.qiuyongchen.windweibo.UI;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.qiuyongchen.windweibo.R;
import com.qiuyongchen.windweibo.adapter.WeiboStatusAdapter;
import com.qiuyongchen.windweibo.logic.MainService;
import com.qiuyongchen.windweibo.weibo.Constants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

/**
 * Home界面
 * Created by qiuyongchen on 2016/2/27.
 */
public class HomeActivity extends Activity implements BaseActivity {
    private ListView listView;
    private View progress;
    private View textView;

    // 用于获取微博信息流等操作的API
    private StatusesAPI mStatusesAPI;

    // 微博列表适配器
    private WeiboStatusAdapter weiboStatusAdapter;
    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {
                        Toast.makeText(HomeActivity.this,
                                "获取微博信息流成功, 条数: " + statuses.statusList.size(),
                                Toast.LENGTH_LONG).show();

                        // 刷新微博列表
                        weiboStatusAdapter = new WeiboStatusAdapter(HomeActivity.this, statuses.statusList);

                        listView.setAdapter(weiboStatusAdapter);

                        progress.setVisibility(View.GONE);
                    }

                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                    Status status = Status.parse(response);

                } else {
                    Toast.makeText(HomeActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    @Override
    public void init() {
        listView = (ListView) findViewById(R.id.lv_weibo_status);
        progress = findViewById(R.id.layout_progress_refresh);
        textView = findViewById(R.id.layout_title);

        // 显示用户昵称
        // ((TextView) textView.findViewById(R.id.tv_screen_name)).setText(MainService.accessToken.getUid());

        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, MainService.accessToken);
        mStatusesAPI.friendsTimeline(0L, 0L, 20, 1, false, 0, false, mListener);
    }

    @Override
    public void refresh() {

    }
}
