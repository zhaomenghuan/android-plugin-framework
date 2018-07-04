package cn.com.agree.hook.startActivityHook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.Method;

import cn.com.agree.hook.log.Logger;

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

    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
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
