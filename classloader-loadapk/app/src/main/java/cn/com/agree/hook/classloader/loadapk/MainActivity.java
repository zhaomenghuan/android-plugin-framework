package cn.com.agree.hook.classloader.loadapk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;

import cn.com.agree.hook.classloader.loadapk.ams_hook.ActivityThreadHookHelper;
import cn.com.agree.hook.classloader.loadapk.classloder_hook.LoadedApkClassLoaderHookHelper;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "ClassLoader";
    private Context mContext;

    /**
     * 这个方法比onCreate调用早; 在这里Hook比较好.
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

        ActivityThreadHookHelper.doHandlerHook();

        try {
            // 拷贝插件 APK
            Utils.copyAssetFile(newBase, "PluginApp.apk");
            String pluginAppPath = newBase.getExternalFilesDir("plugin").getAbsolutePath() + File.separator + "PluginApp.apk";
            // Hook LoadedApk
            LoadedApkClassLoaderHookHelper.hookLoadedApkInActivityThread(new File(pluginAppPath));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        // Activity 类的 startActivity
        ActivityThreadHookHelper.doActivityStartHook(this);

        // 无需 hook 直接使用 DexClassLoader 加载 dex 文件
        findViewById(R.id.loadDexFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.copyAssetFile(mContext, "plugin.dex");
                Utils.DexClassLoader(mContext, "plugin.dex");
            }
        });

        // 打开插件的 Activity
        findViewById(R.id.installPlugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("cn.com.agree.sdk.pluginapp", "cn.com.agree.sdk.pluginapp.MainActivity"));
                startActivity(intent);
            }
        });
    }
}