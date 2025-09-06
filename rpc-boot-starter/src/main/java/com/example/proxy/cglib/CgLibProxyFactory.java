package com.example.proxy.cglib;

import com.example.annotation.RpcReference;
import com.example.proxy.IProxy;
import net.sf.cglib.proxy.Enhancer;

public class CgLibProxyFactory<T> implements IProxy {

    public <T> T getProxy(Class claz, RpcReference rpcReference)  {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(claz);
        enhancer.setCallback(new CgLibProxy(claz,rpcReference));
        return (T) enhancer.create();
    }
}
