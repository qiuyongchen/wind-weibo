package com.qiuyongchen.windweibo.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qiuyongchen.windweibo.R;
import com.qiuyongchen.windweibo.bean.Task;
import com.qiuyongchen.windweibo.logic.MainService;

/**
 * @author qiuyongchen
 */
public class LoginActivity extends Activity implements BaseActivity {

    private TextView tvShow;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvShow = (TextView) findViewById(R.id.tvShow);

        // 启动Mainservice
        Intent intent = new Intent(LoginActivity.this, MainService.class);
        startService(intent);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 给逻辑层传去一个登录任务
                Task task = new Task(Task.LOGIN_WEIBO, null);
                // MainService.newTask(task);

                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 把当前UI加到Mainservice的UI集合中
        MainService.addActivity(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void refresh() {
        tvShow.setText("refresh...");
    }
}
