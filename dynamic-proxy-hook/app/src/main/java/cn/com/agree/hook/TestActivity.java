package cn.com.agree.hook;

import android.app.Activity;
import android.os.Bundle;

import cn.com.agree.dynamic_proxy_hook.R;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/7/2.
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}