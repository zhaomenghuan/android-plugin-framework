package cn.com.agree.startActivityHook;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActivityThreadHookHelper {
    /**
     * 这里hook了以Activity.StartActivity方式启动的方法
     *
     * @param activity
     */
    public static void doActivityStartHook(Activity activity) {
        try {
            // 拿到原始的 mInstrumentation字段
            Field mInstrumentationField = Activity.class.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);

            // 创建代理对象
            Instrumentation originalInstrumentation = (Instrumentation) mInstrumentationField.get(activity);
            mInstrumentationField.set(activity, new EvilInstrumentation(originalInstrumentation));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将替换的activity在此时还原回来
     */
    public static void doHandlerHook() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object activityThread = currentActivityThread.invoke(null);

            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            Handler mH = (Handler) mHField.get(activityThread);

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH, new ActivityThreadHandlerCallback(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}