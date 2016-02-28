package com.qiuyongchen.windweibo.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.qiuyongchen.windweibo.R;
import com.qiuyongchen.windweibo.adapter.WeiboStatusAdapter;
import com.qiuyongchen.windweibo.bean.Task;
import com.qiuyongchen.windweibo.logic.MainService;
import com.sina.weibo.sdk.openapi.models.StatusList;

/**
 * Home界面
 * Created by qiuyongchen on 2016/2/27.
 */
public class HomeActivity extends Activity implements BaseActivity {
    private ListView listView;
    private View progress;
    private View textView;



    // 微博列表适配器
    private WeiboStatusAdapter weiboStatusAdapter;


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

        MainService.addActivity(this);

        getFriendsTimeLine();
    }

    @Override
    public void refresh(Object... params) {
        Toast.makeText(getApplicationContext(),
                "获取微博信息流成功, 条数: " + ((StatusList) params[0]).statusList.size(),
                Toast.LENGTH_LONG).show();

        // 刷新微博列表
        weiboStatusAdapter = new WeiboStatusAdapter(HomeActivity.this, ((StatusList) params[0]).statusList);

        listView.setAdapter(weiboStatusAdapter);

        progress.setVisibility(View.GONE);
    }

    // 异步获取最新微博
    private void getFriendsTimeLine() {
        Task task = new Task(Task.FRIENDS_TIMELINE, null);
        MainService.newTask(task);
    }

}
