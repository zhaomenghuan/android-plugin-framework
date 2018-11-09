package cn.com.agree.hook.classloader.loadapk.classloder_hook;

import dalvik.system.DexClassLoader;

/**
 * 自定义的ClassLoader, 用于加载"插件"的资源和代码
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/11/9.
 */
public class CustomClassLoader extends DexClassLoader {
    public CustomClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, libraryPath, parent);
    }
}
