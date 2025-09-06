package org.example.service;

import org.example.annotation.RpcService;

@RpcService
public class HelloService implements Comparable, IHelloService {

    @Override
    public Object hello(String text) {
        return "service1 result:" + text;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}

