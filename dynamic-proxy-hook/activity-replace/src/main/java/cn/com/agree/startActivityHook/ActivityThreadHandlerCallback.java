package cn.com.agree.startActivityHook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;

import cn.com.agree.log.Logger;

public class ActivityThreadHandlerCallback implements Handler.Callback {
    private Handler mBaseHandler;

    public ActivityThreadHandlerCallback(Handler mBaseHandler) {
        this.mBaseHandler = mBaseHandler;
    }

    /**
     * @param msg 源码路径:/frameworks/base/core/java/android/app/ActivityThread.java
     * 1294 public void handleMessage(Message msg) {
     * 1295   if (DEBUG_MESSAGES) Slog.v(TAG, ">>> handling: " + codeToString(msg.what));
     * 1296     switch (msg.what) {
     * 1297       case LAUNCH_ACTIVITY: {
     * 1298         Trace.traceBegin(Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
     * 1299         final ActivityClientRecord r = (ActivityClientRecord) msg.obj;
     * 1300
     * 1301         r.packageInfo = getPackageInfoNoCheck(
     * 1302         r.activityInfo.applicationInfo, r.compatInfo);
     * 1303         handleLaunchActivity(r, null);
     * 1304         Trace.traceEnd(Trace.TRACE_TAG_ACTIVITY_MANAGER);
     * 1305       } break;
     *       ...
     *          }
     */
    @Override
    public boolean handleMessage(Message msg) {
        Logger.i(ActivityThreadHandlerCallback.class, "接受到消息了msg:" + msg);

        if (msg.what == 100) {
            try {
                Object obj = msg.obj;
                Field intentField = obj.getClass().getDeclaredField("intent");
                intentField.setAccessible(true);
                Intent intent = (Intent) intentField.get(obj);

                Intent targetIntent = intent.getParcelableExtra(StubActivity.TARGET_COMPONENT);
                intent.setComponent(targetIntent.getComponent());
                Log.e("intentField", targetIntent.toString());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        mBaseHandler.handleMessage(msg);
        return true;
    }
}
