package org.example.proxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import org.example.proxy.IProxy;

public class CgLibProxyFactory<T> implements IProxy {

    @Override
    public <T> T getProxy(Class<T> claz) throws InstantiationException, IllegalAccessException {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(claz);
        enhancer.setCallback(new CgLibProxy(claz.newInstance()));
        return (T) enhancer.create();
    }
}
