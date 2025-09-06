package org.example.service;

import org.example.annotation.RpcService;

@RpcService
public class HelloService implements IHelloService{

    @Override
    public Object hello(String text) {
        String s = null;
        s.length();
        return "service1 result:"+text;
    }
}
