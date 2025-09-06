package com.example.proxy;

import com.example.annotation.RpcReference;

public interface IProxy {

    <T> T getProxy(Class claz, RpcReference rpcReference);
}
