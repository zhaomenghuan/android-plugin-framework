package cn.com.agree.hook.reflect;

import cn.com.agree.hook.log.Logger;

/**
 * 被反射测试的类
 */
public class BeReflected {
    private String field1 = "I am field1";
    private String field2 = "I am field2";
    private static String staticField = "I am staticField";
    private void method1(){
        Logger.i(BeReflected.class, "I am method1");
    }
    private void method1(String param) {
        Logger.i(BeReflected.class, "I am method1--param = " + param);
    }
    private void method2(){
        Logger.i(BeReflected.class, "I am method2");
    }
    public static void staticMethod(){
        Logger.i(BeReflected.class, "I am staticMethod");
    }
}
