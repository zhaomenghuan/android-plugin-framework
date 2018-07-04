package cn.com.agree.hook.proxy.staticproxy;

import cn.com.agree.hook.log.Logger;

public class RealSubject1 implements Subject1 {
    @Override
    public void method1() {
        Logger.i(RealSubject1.class, "我是RealSubject1的方法1");
    }

    @Override
    public void method2() {
        Logger.i(RealSubject1.class, "我是RealSubject1的方法2");
    }
}