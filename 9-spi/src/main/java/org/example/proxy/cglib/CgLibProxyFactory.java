package org.example.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import org.example.proxy.IProxy;

/**
 * 动态代理工厂
 * @param <T>
 */
public class CgLibProxyFactory<T> implements IProxy {

    /**
     * 返回类对应的动态代理类对象
     * @param claz
     * @return
     * @param <T>
     */
    public <T> T getProxy(Class<T> claz)  {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(claz);
        enhancer.setCallback(new CgLibProxy(claz));
        return (T) enhancer.create();
    }
}
