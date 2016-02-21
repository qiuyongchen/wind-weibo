package com.qiuyongchen.windweibo.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qiuyongchen.windweibo.R;

import java.util.Objects;


/**
 * 新浪微博的认证
 */
public class WebViewActivity extends Activity {

    // 加载完毕
    private static final int LOAD_FINISH = 1;

    // 负责加载页面的浏览器
    private WebView webView;

    // 后台加载时，UI上的实时进度框
    private ProgressDialog progressDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            // 加载URL完毕后，就关闭进度框
            if (msg.what == LOAD_FINISH) {
                progressDialog.dismiss();
            }
        }
    };
    private String URL = "http://www.qiuyongchen.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webView);

        init();

        Log.e("WebViewActivity", "onCreate init();");

        loadUrl(URL, webView);
    }

    public void init() {

        // 后台数据加载的同时，在UI上显示示一个进度对话框
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        progressDialog.show();

        // 允许javascript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadUrl(URL, webView);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // 加载是否完毕
                if (newProgress == 100) {
                    handler.sendEmptyMessage(LOAD_FINISH);
                }

            }
        });
    }

    public void loadUrl(final String url, final WebView view) {
        if (url == null || Objects.equals(url, "")) {
            return;
        }

        view.loadUrl(url);
        Log.e("WebViewActivity", "loadUrl view.loadUrl(url);");

        // 不能开启一个线程来加载URL，因为现在的WebView（2016年2月21日 21:45:47）只能在UI线程中跑
        /*new Thread() {
            @Override
            public void run() {

            }
        }.start();*/
    }
}
