package com.example.invoke;

import com.example.common.constants.RpcInvoker;
import com.example.spi.ExtensionLoader;

import java.io.IOException;

public class InvokerFactory {


    public static Invoker get(RpcInvoker rpcInvoker){
        return ExtensionLoader.getInstance().get(rpcInvoker.name);
    }

    public static Invoker get(String name){

        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(Invoker.class);
    }
}
