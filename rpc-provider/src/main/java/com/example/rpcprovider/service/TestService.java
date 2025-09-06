package com.example.rpcprovider.service;

import com.example.annotation.RpcService;
import org.example.service.HelloService;
import org.springframework.stereotype.Component;

@Component
@RpcService
public class TestService implements HelloService {
    @Override
    public Object hello(String arg) {
        // 手动模拟下报错
        String s = null;
        s.length();
        return arg + "provider1";
    }
}
