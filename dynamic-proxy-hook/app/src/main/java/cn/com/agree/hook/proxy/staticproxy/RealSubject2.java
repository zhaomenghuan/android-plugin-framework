package cn.com.agree.hook.proxy.staticproxy;

import cn.com.agree.hook.log.Logger;

public class RealSubject2 implements Subject2 {
    @Override
    public void method1() {
        Logger.i(RealSubject2.class, "我是RealSubject2的方法1");
    }

    @Override
    public void method2() {
        Logger.i(RealSubject2.class, "我是RealSubject2的方法2");
    }
}