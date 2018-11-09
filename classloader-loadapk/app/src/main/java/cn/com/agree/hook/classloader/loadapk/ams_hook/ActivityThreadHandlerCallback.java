package cn.com.agree.hook.classloader.loadapk.ams_hook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Proxy;

import cn.com.agree.hook.classloader.loadapk.RefInvoke;

public class ActivityThreadHandlerCallback implements Handler.Callback {
    private static String TAG = ActivityThreadHandlerCallback.class.getName();
    private Handler mBaseHandler;

    public ActivityThreadHandlerCallback(Handler mBaseHandler) {
        this.mBaseHandler = mBaseHandler;
    }

    /**
     * @param msg 源码路径:/frameworks/base/core/java/android/app/ActivityThread.java
     *            1294 public void handleMessage(Message msg) {
     *            1295   if (DEBUG_MESSAGES) Slog.v(TAG, ">>> handling: " + codeToString(msg.what));
     *            1296     switch (msg.what) {
     *            1297       case LAUNCH_ACTIVITY: {
     *            1298         Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
     *            1299         final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
     *            1300
     *            1301         r.packageInfo = getPackageInfoNoCheck(
     *            1302         r.activityInfo.applicationInfo, r.compatInfo);
     *            1303         handleLaunchActivity(r, null);
     *            1304         Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
     *            1305       } break;
     *            ...
     *            }
     */
    @Override
    public boolean handleMessage(Message msg) {
        Log.i(TAG, "接受到消息了msg:" + msg);

        if (msg.what == 100) {
            try {
                Object obj = msg.obj;
                Intent intent = (Intent) RefInvoke.getFieldObject(obj, "intent");
                Intent targetIntent = intent.getParcelableExtra(StubActivity.TARGET_COMPONENT);
                intent.setComponent(targetIntent.getComponent());

                // 根据 getPackageInfo 根据这个 包名获取 LoadedApk的信息; 因此这里我们需要手动填上, 从而能够命中缓存
                ActivityInfo activityInfo = (ActivityInfo) RefInvoke.getFieldObject(obj, "activityInfo");
                activityInfo.applicationInfo.packageName = targetIntent.getPackage() == null ? targetIntent.getComponent().getPackageName() : targetIntent.getPackage();

                hookPackageManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mBaseHandler.handleMessage(msg);
        return true;
    }

    public static void hookPackageManager() {
        try {
            // 获取全局的ActivityThread对象
            Object currentActivityThread = RefInvoke.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");

            // 获取ActivityThread里面原始的 sPackageManager
            Object sPackageManager = RefInvoke.getFieldObject(currentActivityThread, "sPackageManager");

            // 准备好代理对象, 用来替换原始的对象
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(),
                    new Class<?>[]{iPackageManagerInterface},
                    new IPackageManagerHookHandler(sPackageManager));

            // 替换掉ActivityThread里面的 sPackageManager 字段
            RefInvoke.setFieldObject(currentActivityThread, "sPackageManager", proxy);
        } catch (Exception e) {
            throw new RuntimeException("hook failed", e);
        }
    }
}
