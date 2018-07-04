package cn.com.agree.hook.startActivityHook;

import android.app.Activity;
import android.app.Instrumentation;

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
}