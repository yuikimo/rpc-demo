package org.example.proxy;

import org.example.common.constants.RpcProxy;
import org.example.proxy.cglib.CgLibProxyFactory;

import java.util.HashMap;
import java.util.Map;

public class ProxyFactory {

    private static Map<RpcProxy, IProxy> proxyIProxyMap = new HashMap<RpcProxy, IProxy>();

    static {
        proxyIProxyMap.put(RpcProxy.CG_LIB, new CgLibProxyFactory());
    }

    public static IProxy get(RpcProxy rpcProxy) {
        return proxyIProxyMap.get(rpcProxy);
    }
}
