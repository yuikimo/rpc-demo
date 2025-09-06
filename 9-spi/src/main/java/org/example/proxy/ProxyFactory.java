package org.example.proxy;

import org.example.common.constants.RpcProxy;
import org.example.spi.ExtensionLoader;

import java.io.IOException;

public class ProxyFactory {

    public static IProxy get(RpcProxy rpcProxy){
        return ExtensionLoader.getInstance().get(rpcProxy.name);

    }

    public static IProxy get(String name){
        return ExtensionLoader.getInstance().get(name);

    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(IProxy.class);
    }
}
