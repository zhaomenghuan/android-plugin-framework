package cn.com.agree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.com.agree.startActivityHook.ActivityThreadHookHelper;
import cn.com.agree.startActivityHook.R;

public class MainActivity extends Activity {
    private Button activityStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Activity 类的 startActivity
        ActivityThreadHookHelper.doActivityStartHook(this);
        activityStart = findViewById(R.id.activityStart);
        activityStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        ActivityThreadHookHelper.doHandlerHook();
    }
}