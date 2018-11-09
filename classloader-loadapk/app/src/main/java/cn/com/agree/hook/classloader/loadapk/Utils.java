package cn.com.agree.hook.classloader.loadapk;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/11/8.
 */

public class Utils {
    /**
     * 使用DexClassLoader方式加载类
     */
    public static void DexClassLoader(Context context, String src) {
        // dex压缩文件的路径（可以是 apk,jar,zip 格式）
        String dexPath = context.getExternalFilesDir("plugin").getAbsolutePath() + File.separator + src;
        // dex解压释放后的目录
        File dexOutputDirFile = context.getDir("dex", 0);

        // 定义DexClassLoader
        // 第一个参数：是dex压缩文件的路径
        // 第二个参数：是dex解压缩后存放的目录
        // 第三个参数：是C/C++依赖的本地库文件目录,可以为null
        // 第四个参数：是上一级的类加载器
        DexClassLoader dexClassLoader = new DexClassLoader(dexPath, dexOutputDirFile.getAbsolutePath(), null, context.getClassLoader());
        // 使用DexClassLoader加载类
        try {
            Class<?> clz = dexClassLoader.loadClass("cn.com.agree.plugin.ToastUtil");
            Object instance = clz.newInstance();
            Method showToast = clz.getMethod("show", Context.class);
            showToast.invoke(instance, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 拷贝文件
     *
     * @param context
     * @param sourceName
     */
    public static void copyAssetFile(Context context, String sourceName) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = context.getAssets().open(sourceName);
            out = new FileOutputStream(context.getExternalFilesDir("plugin").getAbsolutePath() + File.separator + sourceName);
            byte[] buffer = new byte[4096];
            int length = 0;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 需要加载得插件得基本目录 /Android/data/<package>/files/plugin/
     *
     * @param packageName
     * @return
     */
    private static File getPluginBaseDir(Context context, String packageName) {
        File sBaseDir = context.getExternalFilesDir("plugin");
        enforceDirExists(sBaseDir);
        return enforceDirExists(new File(sBaseDir, packageName));
    }

    /**
     * 待加载插件经过opt优化之后存放odex得路径
     *
     * @param context
     * @param packageName
     * @return
     */
    public static File getPluginOptDexDir(Context context, String packageName) {
        return enforceDirExists(new File(getPluginBaseDir(context, packageName), "odex"));
    }

    /**
     * 插件得lib库路径
     */
    public static File getPluginLibDir(Context context, String packageName) {
        return enforceDirExists(new File(getPluginBaseDir(context, packageName), "lib"));
    }

    /**
     * 检查文件夹
     *
     * @param sBaseDir
     * @return
     */
    private static synchronized File enforceDirExists(File sBaseDir) {
        if (!sBaseDir.exists()) {
            boolean ret = sBaseDir.mkdir();
            if (!ret) {
                throw new RuntimeException("create dir " + sBaseDir + "failed");
            }
        }
        return sBaseDir;
    }
}
