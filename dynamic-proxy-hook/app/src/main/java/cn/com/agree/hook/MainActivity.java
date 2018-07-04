package cn.com.agree.hook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.com.agree.dynamic_proxy_hook.R;
import cn.com.agree.hook.startActivityHook.ActivityThreadHookHelper;

public class MainActivity extends AppCompatActivity {
    private Button contextStart;
    private Button activityStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityThreadHookHelper.doActivityStartHook(this);

        contextStart = findViewById(R.id.contextStart);
        activityStart = findViewById(R.id.activityStart);

        contextStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });

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
        ActivityThreadHookHelper.doContextStartHook();
    }
}