package cn.com.agree.startActivityHook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.Method;

import cn.com.agree.log.Logger;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/7/2.
 */
public class EvilInstrumentation extends Instrumentation {
    private Instrumentation instrumentation;

    public EvilInstrumentation(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {

        StringBuilder sb = new StringBuilder();
        sb.append("who = [").append(who).append("], ")
        .append("contextThread = [").append(contextThread).append("], ")
        .append("token = [").append(token).append("], ")
        .append("target = [").append(target).append("], ")
        .append("intent = [").append(intent).append("], ")
        .append("requestCode = [").append(requestCode).append("], ")
        .append("options = [").append(options).append("]");;
        Logger.i(EvilInstrumentation.class, "执行了startActivity, 参数如下: " + sb.toString());

        // 在此处先将 intent 原本的 Component 保存起来,
        // 然后创建一个新的 intent 只想 StubActivity 并替换掉原本的 Activity,以达通过 ams 验证的目的,然后等 ams 验证通过后再将其还原。
        // 方法一: 在此替换虽然非常简单,但此处很明显不是一个好的hook点,因为 mInstrumentation 是 Activity 的成员变量,但是在程序中 Activity 并不是一个,而是有多个实例,所以需要在每个实例中 hook 掉才可以。
        Intent replaceIntent = new Intent(target, StubActivity.class);
        replaceIntent.putExtra(StubActivity.TARGET_COMPONENT, intent);
        intent = replaceIntent;

        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity",
                    Context.class,
                    IBinder.class,
                    IBinder.class,
                    Activity.class,
                    Intent.class,
                    int.class,
                    Bundle.class);
            return (ActivityResult) execStartActivity.invoke(instrumentation, who, contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}