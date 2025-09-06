package com.example.rpcprovider2.service;

import com.example.annotation.RpcService;
import org.example.service.HelloService;
import org.springframework.stereotype.Component;

@Component
@RpcService
public class TestService implements HelloService {
    @Override
    public Object hello(String arg) {
        return arg + "provider2";
    }
}
