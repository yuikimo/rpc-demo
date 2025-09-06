package org.example.proxy.cglib;


import net.sf.cglib.proxy.Enhancer;
import org.example.proxy.IProxy;

public class CgLibProxyFactory<T> implements IProxy {


    public <T> T getProxy(Class<T> claz) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(claz);
        enhancer.setCallback(new CgLibProxy(claz));
        return (T) enhancer.create();
    }
}
