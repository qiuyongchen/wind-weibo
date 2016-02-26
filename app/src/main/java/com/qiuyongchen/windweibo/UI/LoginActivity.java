package com.qiuyongchen.windweibo.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qiuyongchen.windweibo.R;
import com.qiuyongchen.windweibo.adapter.UserInfoAdapter;
import com.qiuyongchen.windweibo.bean.Task;
import com.qiuyongchen.windweibo.bean.UserInfo;
import com.qiuyongchen.windweibo.db_services.UserInfoServices;
import com.qiuyongchen.windweibo.logic.MainService;
import com.qiuyongchen.windweibo.weibo.AccessTokenKeeper;
import com.qiuyongchen.windweibo.weibo.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.User;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.qiuyongchen.windweibo.weibo.AccessTokenKeeper.readAccessToken;

/**
 * @author qiuyongchen
 */
public class LoginActivity extends Activity implements BaseActivity {

    private TextView tvShow;
    // 用户头像
    private ImageView imageViewHead;
    // 用户昵称
    private EditText editTextUserName;
    private Button btnMore;
    private LoginButton btnLoginDefault;
    // 登录按钮
    private Button btnLogin;

    // 记录新浪登录所需的APPKEY(开发者应用信息)
    private AuthInfo mAuthInfo;
    // 用户信息接口(用户信息)
    private UsersAPI mUsersAPI;
    // 授权信息(access_token)
    private Oauth2AccessToken accessToken;
    private UserInfoServices userInfoServices;

    //  登陆认证对应的listener
    private AuthListener mLoginListener = new AuthListener();
    // 登出操作对应的listener
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();
    // 微博 OpenAPI 回调接口。
    private RequestListener requestListener = new RequestListener() {
        @Override
        public void onComplete(final String response) {
            if (!TextUtils.isEmpty(response)) {
                LogUtil.i("LoginActivity", response);

                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);

                Map<String, Object> taskParams = new HashMap<>();
                taskParams.put("User", user);
                Task task = new Task(Task.AUTH_WEIBO, taskParams);

                MainService.newTask(task);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            LogUtil.e("LoginActivity", e.getMessage());
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(LoginActivity.this, info != null ? info.toString() : null, Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 如果已经有用户登录过，那就自动登录
        accessToken = readAccessToken(getApplicationContext());
        if (!accessToken.getUid().equals("UID is not exists")) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        tvShow = (TextView) findViewById(R.id.tvShow);
        imageViewHead = (ImageView) findViewById(R.id.img_user_head);
        editTextUserName = (EditText) findViewById(R.id.editTextName);
        btnMore = (Button) findViewById(R.id.btnMore);
        btnLoginDefault = (LoginButton) findViewById(R.id.btnLoginDefault);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        // 创建授权认证信息
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);

        // 微博授权按钮监听器
        btnLoginDefault.setWeiboAuthInfo(mAuthInfo, mLoginListener);

        // 登录按钮监听器
        btnLogin.setOnClickListener(new btnLoginOnClickListener());

        // 启动Mainservice
        Intent intent = new Intent(LoginActivity.this, MainService.class);
        startService(intent);

        // 把当前UI加到Mainservice的UI集合中
        MainService.addActivity(this);

        init();

    }

    @Override
    public void init() {
        userInfoServices = new UserInfoServices(this);

        List<UserInfo> userInfos = userInfoServices.getAllUser();

        if (!userInfoServices.isEmpty()) {
            btnMore.setOnClickListener(new btnMoreOnClickListener());
        }
    }

    @Override
    public void refresh() {
        tvShow.setText("头像和昵称已更新...");
        btnMore.setOnClickListener(new btnMoreOnClickListener());
    }

    /**
     * 登入按钮的监听器，接收授权结果。
     * 然后进一步获取用户信息
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                tvShow.setText(String.format(format, accessToken.getToken(), date));

                // 如果数据库中还没有这个用户，就将用户保存起来，并下载用户头像
                UserInfo userInfo = new UserInfo(accessToken.getUid(), accessToken.getUid(),
                        accessToken.getToken(), String.valueOf(accessToken.getExpiresTime()),
                        accessToken.getRefreshToken(), "no");
                UserInfoServices userInfoServices = new UserInfoServices(getApplicationContext());

                if (!userInfoServices.exists(userInfo)) {

                    userInfoServices.insertUser(userInfo);

                    // 获取用户信息接口
                    mUsersAPI = new UsersAPI(LoginActivity.this, Constants.APP_KEY, accessToken);

                    long uid = Long.parseLong(accessToken.getUid());
                    mUsersAPI.show(uid, requestListener);
                }
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

    /**
     * 点击按钮，将弹出用户列表的对话框
     */
    private class btnMoreOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            View view = View.inflate(LoginActivity.this, R.layout.dialog_users_select, null);
            final Dialog dialog = new Dialog(LoginActivity.this, R.style.dialog_style_user_select);

            dialog.setContentView(view);
            dialog.show();

            ListView listView = (ListView) view.findViewById(R.id.lv_user_list);
            UserInfoAdapter userInfoAdapter = new UserInfoAdapter(LoginActivity.this, userInfoServices.getAllUser());
            listView.setAdapter(userInfoAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ImageView imageView = (ImageView) view.findViewById(R.id.imageView_user_head);
                    TextView textView = (TextView) view.findViewById(R.id.tv_user_name);

                    imageViewHead.setImageDrawable(imageView.getDrawable());
                    editTextUserName.setText(textView.getText());

                    // 关闭对话框
                    dialog.dismiss();
                }
            });
        }
    }

    /**
     * 点击按钮，将会保存当前登录用户，并跳转到主界面
     */
    private class btnLoginOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            AccessTokenKeeper.writeAccessToken(getApplicationContext(), accessToken);

            Map<String, Object> map = new HashMap<>();
            map.put("accessToken", accessToken);
            Task task = new Task(Task.LOGIN_WEIBO, map);
            MainService.newTask(task);

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
