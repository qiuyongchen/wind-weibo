package com.qiuyongchen.windweibo.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.qiuyongchen.windweibo.R;
import com.qiuyongchen.windweibo.bean.Task;
import com.qiuyongchen.windweibo.logic.MainService;
import com.qiuyongchen.windweibo.weibo.AccessTokenKeeper;
import com.qiuyongchen.windweibo.weibo.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * @author qiuyongchen
 */
public class LoginActivity extends Activity implements BaseActivity {

    private TextView tvShow;
    private Button btnLogin;

    private LoginButton btnLoginDefault;

    //  登陆认证对应的listener
    private AuthListener mLoginListener = new AuthListener();

    // 登出操作对应的listener
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

    // 记录新浪登录所需的APPKEY
    private AuthInfo mAuthInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvShow = (TextView) findViewById(R.id.tvShow);
        btnLoginDefault = (LoginButton) findViewById(R.id.btnLoginDefault);

        // 创建授权认证信息
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

        btnLoginDefault.setWeiboAuthInfo(mAuthInfo, mLoginListener);





        // 启动Mainservice
        Intent intent = new Intent(LoginActivity.this, MainService.class);
        // startService(intent);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 给逻辑层传去一个登录任务
                Task task = new Task(Task.LOGIN_WEIBO, null);
                // MainService.newTask(task);

                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                // startActivity(intent);
                // finish();
            }
        });

        // 把当前UI加到Mainservice的UI集合中
        // MainService.addActivity(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void refresh() {
        tvShow.setText("refresh...");
    }

    /**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                tvShow.setText(String.format(format, accessToken.getToken(), date));

                AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        AccessTokenKeeper.clear(LoginActivity.this);
                        tvShow.setText(R.string.weibosdk_demo_logout_success);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            tvShow.setText(R.string.weibosdk_demo_logout_failed);
        }
    }
}
