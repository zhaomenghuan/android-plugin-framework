package cn.com.agree.hook.classloader.loadapk.classloder_hook;

import android.content.pm.ApplicationInfo;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cn.com.agree.hook.classloader.loadapk.RefInvoke;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/11/8.
 */

public class LoadedApkClassLoaderHookHelper {
    private static String TAG = LoadedApkClassLoaderHookHelper.class.getName();
    public static Map<String, Object> sLoadedApk = new HashMap<String, Object>();

    public static void hookLoadedApkInActivityThread(File apkFile) throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException, InstantiationException {

        // 先获取到当前的ActivityThread对象
        Object currentActivityThread = RefInvoke.getStaticFieldObject("android.app.ActivityThread", "sCurrentActivityThread");
        // 获取包的缓存对象
        Map mPackages = (Map) RefInvoke.getFieldObject(currentActivityThread, "mPackages");

        // 获取 ApplicationInfo
        ApplicationInfo applicationInfo = generateApplicationInfo(apkFile);

        // 获取 CompatibilityInfo
        Class<?> compatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
        Object defaultCompatibilityInfo = RefInvoke.getStaticFieldObject(compatibilityInfoClass, "DEFAULT_COMPATIBILITY_INFO");

        // 构建 LoadedApk
        Object loadedApk = RefInvoke.invokeInstanceMethod(currentActivityThread, "getPackageInfoNoCheck",
                new Class[]{ApplicationInfo.class, compatibilityInfoClass},
                new Object[]{applicationInfo, defaultCompatibilityInfo});

        // 由于是弱引用, 因此我们必须在某个地方存一份, 不然容易被GC; 那么就前功尽弃了.
        sLoadedApk.put(applicationInfo.packageName, loadedApk);

        WeakReference weakReference = new WeakReference(loadedApk);
        mPackages.put(applicationInfo.packageName, weakReference);
    }

    /**
     * 反射 android.content.pm.PackageParser#generateApplicationInfo 方法，得到ApplicationInfo对象
     * <p>
     * generateApplicationInfo方法：
     * <p>
     * public static ApplicationInfo generateApplicationInfo(Package p, int flags,PackageUserState state, int userId)
     * <p>
     * 这个方法需要Package参数和PackageUserState参数
     */
    public static ApplicationInfo generateApplicationInfo(File apkFile)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException {

        // 找出需要反射的核心类: android.content.pm.PackageParser
        Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");
        // 获取PackageParser$Package类
        Class<?> packageParser$PackageClass = Class.forName("android.content.pm.PackageParser$Package");
        Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");

        Method generateApplicationInfoMethod = packageParserClass.getDeclaredMethod("generateApplicationInfo",
                packageParser$PackageClass,
                int.class,
                packageUserStateClass);


        // 首先, 我们得创建出一个Package对象出来供这个方法调用
        // 而这个需要得对象可以通过 android.content.pm.PackageParser#parsePackage 这个方法返回得 Package对象得字段获取得到
        // 创建出一个PackageParser对象供使用
        Object packageParser = packageParserClass.newInstance();
        // 调用 PackageParser.parsePackage 解析apk的信息
        Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);

        // 实际上是一个 android.content.pm.PackageParser.Package 对象
        Object packageObj = parsePackageMethod.invoke(packageParser, apkFile, 0);

        // 第三个参数 mDefaultPackageUserState 我们直接使用默认构造函数构造一个出来即可
        Object defaultPackageUserState = packageUserStateClass.newInstance();

        // 反射 generateApplicationInfo 得到ApplicationInfo对象
        ApplicationInfo applicationInfo = (ApplicationInfo) generateApplicationInfoMethod.invoke(packageParser,
                packageObj, 0, defaultPackageUserState);
        String apkPath = apkFile.getPath();

        applicationInfo.sourceDir = apkPath;
        applicationInfo.publicSourceDir = apkPath;

        return applicationInfo;
    }
}
