package cn.com.agree.startActivityHook;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ActivityThreadHookHelper {
    /**
     * 这里hook了以Context.startActivity方式启动的方法
     */
    public static void doContextStartHook() {
        try {
            // 先获取到当前的ActivityThread对象
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");

            /**
             * 查看源码ActivityThread的源码可以发现,此类是一个单例,并却由currentActivityThread()获得对象
             * public static ActivityThread currentActivityThread() {
             *   return sCurrentActivityThread;
             * }
             */
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            // currentActivityThread是一个static函数所以可以直接invoke，不需要带实例参数
            Object activityThread = currentActivityThreadMethod.invoke(null);

            // 拿到原始的 mInstrumentation字段
            Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            // 创建代理对象
            Instrumentation originalInstrumentation = (Instrumentation) mInstrumentationField.get(activityThread);
            mInstrumentationField.set(activityThread, new EvilInstrumentation(originalInstrumentation));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
            Handler mH = (Handler)mHField.get(activityThread);

            // 1、ActivityThreadHandler是Handler的子类,在这替换时我们发现会抛出异常:
            // java.lang.IllegalArgumentException: field android.app.ActivityThread.mH has type android.app.ActivityThread$H,
            // got com.liuwei.activity_replace.hook.ActivityThreadHandler
            // 这显然是不行的,因为ActvitiyTreadHandler是不能转换成ActivityThread$H类型的。
            // mHField.set(activityThread, new ActivityThreadHandler(mH));

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            mCallbackField.set(mH, new ActivityThreadHandlerCallback(mH));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}