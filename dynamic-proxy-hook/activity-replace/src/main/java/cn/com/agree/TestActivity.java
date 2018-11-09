package cn.com.agree;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.com.agree.startActivityHook.R;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/7/2.
 */
public class TestActivity extends Activity {
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mContext = this;

        findViewById(R.id.showToast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "这是没有在 AndroidManifest.xml 注册的 Activity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}