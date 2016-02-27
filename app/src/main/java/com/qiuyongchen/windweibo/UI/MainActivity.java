package com.qiuyongchen.windweibo.UI;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TabHost;

import com.qiuyongchen.windweibo.R;

/**
 * 主界面
 * Created by qiuyongchen on 2016/2/21.
 */
public class MainActivity extends TabActivity {

    private static String HOME = "home";
    private static String AT = "at";
    private static String MESSAGE = "message";
    private static String SETTING = "setting";

    private static TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_tab_button);

        tabHost = this.getTabHost();

        TabHost.TabSpec home = tabHost.newTabSpec(HOME).setIndicator(HOME).
                setContent(new Intent(this, HomeActivity.class));
        TabHost.TabSpec at = tabHost.newTabSpec(AT).setIndicator(AT).
                setContent(new Intent(this, AtActivity.class));
        TabHost.TabSpec message = tabHost.newTabSpec(MESSAGE).setIndicator(MESSAGE).
                setContent(new Intent(this, MessageActivity.class));
        TabHost.TabSpec setting = tabHost.newTabSpec(SETTING).setIndicator(SETTING).
                setContent(new Intent(this, SettingActivity.class));

        // 分别绑定4个RadioButton的Tab
        tabHost.addTab(home);
        tabHost.addTab(at);
        tabHost.addTab(message);
        tabHost.addTab(setting);

        // 点击切换Tab事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Log.e("MainActivity", "onCheckedChanged" + String.valueOf(checkedId));

                switch (checkedId) {
                    case R.id.rb_home:
                        tabHost.setCurrentTabByTag(HOME);
                        break;
                    case R.id.rb_at:
                        tabHost.setCurrentTabByTag(AT);
                        break;
                    case R.id.rb_message:
                        tabHost.setCurrentTabByTag(MESSAGE);
                        break;
                    case R.id.rb_setting:
                        tabHost.setCurrentTabByTag(SETTING);
                        break;
                    default:
                        break;
                }
            }
        });

    }
}
