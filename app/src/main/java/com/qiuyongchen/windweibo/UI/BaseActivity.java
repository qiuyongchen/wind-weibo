package com.qiuyongchen.windweibo.UI;

/**
 * 所有UI都要实现这个接口，以便刷新UI
 * Created by qiuyongchen on 2016/2/21.
 */
public interface BaseActivity {

    void init();

    void refresh(Object... params);

}
