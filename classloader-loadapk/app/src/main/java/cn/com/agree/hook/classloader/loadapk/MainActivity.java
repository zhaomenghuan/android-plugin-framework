package cn.com.agree.hook.classloader.loadapk;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "ClassLoader";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        findViewById(R.id.use_plugin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DexClassLoader(mContext);
            }
        });
    }

    /**
     * 使用DexClassLoader方式加载类
     */
    public void DexClassLoader(Context context) {
        // dex压缩文件的路径（可以是 apk,jar,zip 格式）
        String dexPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "plugin.dex";
        // dex解压释放后的目录
        File dexOutputDirFile = context.getDir("dex", MODE_PRIVATE);

        // 定义DexClassLoader
        // 第一个参数：是dex压缩文件的路径
        // 第二个参数：是dex解压缩后存放的目录
        // 第三个参数：是C/C++依赖的本地库文件目录,可以为null
        // 第四个参数：是上一级的类加载器
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexOutputDirFile.getAbsolutePath(), null, getClassLoader());
        // 使用DexClassLoader加载类
        try {
            Class<?> clz = dexClassLoader.loadClass("cn.com.agree.plugin.ToastUtil");
            Object instance  = clz.newInstance();
            Method showToast = clz.getMethod("show", Context.class);
            showToast.invoke(instance, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}