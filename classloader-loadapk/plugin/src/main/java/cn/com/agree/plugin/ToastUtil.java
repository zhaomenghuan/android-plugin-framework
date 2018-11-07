package cn.com.agree.plugin;

import android.content.Context;
import android.widget.Toast;

/**
 * Author: zhaomenghuan
 * Email: zhaomenghuan@agree.com.cn
 * Date：2018/7/19.
 */
public class ToastUtil {
    public void show(Context context) {
        Toast.makeText(context, "我是一个插件", Toast.LENGTH_SHORT).show();
    }
}