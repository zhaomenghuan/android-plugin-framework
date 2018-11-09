package cn.com.agree.hook.classloader.loadapk.ams_hook;

import android.content.pm.PackageInfo;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/11/8.
 */
public class IPackageManagerHookHandler implements InvocationHandler {
    private static final String TAG = "HookHandler";

    private Object mBase;

    public IPackageManagerHookHandler(Object base) {
        mBase = base;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.e(TAG, "method:" + method.getName() + " called with args:" + Arrays.toString(args));

        if (method.getName().equals("getPackageInfo")) {
            return new PackageInfo();
        }

        return method.invoke(mBase, args);
    }
}
