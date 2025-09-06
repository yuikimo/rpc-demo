package org.example.invoke;

import org.example.common.constants.RpcInvoker;

import java.util.HashMap;
import java.util.Map;

public class InvokerFactory {

    // Key：以什么方式调用 Value：具体的实现者
    public static Map<RpcInvoker, Invoker> invokerInvokerMap = new HashMap<>();

    static {
        invokerInvokerMap.put(RpcInvoker.JDK, new JdkReflectionInvoker());
    }

    public static Invoker get(RpcInvoker rpcInvoker) {
        return invokerInvokerMap.get(rpcInvoker);
    }
}
