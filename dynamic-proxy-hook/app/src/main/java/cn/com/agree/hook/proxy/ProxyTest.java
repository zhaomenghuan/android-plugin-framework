package cn.com.agree.hook.proxy;

import cn.com.agree.hook.proxy.dynamicproxy.DynamicProxyHandler;
import cn.com.agree.hook.log.Logger;
import cn.com.agree.hook.proxy.staticproxy.ProxySubject1;
import cn.com.agree.hook.proxy.staticproxy.ProxySubject2;
import cn.com.agree.hook.proxy.staticproxy.RealSubject1;
import cn.com.agree.hook.proxy.staticproxy.RealSubject2;
import cn.com.agree.hook.proxy.staticproxy.Subject1;
import cn.com.agree.hook.proxy.staticproxy.Subject2;

public class ProxyTest {
    public static void main(String[] args) {
        // static proxy
        Subject1 proxySubject1 = new ProxySubject1(new RealSubject1());
        proxySubject1.method1();
        proxySubject1.method2();

        // 如果想对RealSubject2代理显然不得不重新再写一个代理类。
        ProxySubject2 proxySubject2 = new ProxySubject2(new RealSubject2());
        proxySubject2.method1();
        proxySubject2.method2();

        Logger.i(ProxyTest.class, "----------分割线----------\n");

        // 如果写一个代理类就能对上面两个都能代理就好了,动态代理就解决了这个问题
        Subject1 dynamicProxyHandler1 = (Subject1) DynamicProxyHandler.newProxyInstance(new RealSubject1());
        dynamicProxyHandler1.method1();
        dynamicProxyHandler1.method2();

        Subject2 dynamicProxyHandler2 = (Subject2) DynamicProxyHandler.newProxyInstance(new RealSubject2());
        dynamicProxyHandler2.method1();
        dynamicProxyHandler2.method2();
    }
}