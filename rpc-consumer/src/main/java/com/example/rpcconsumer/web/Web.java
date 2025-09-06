package com.example.rpcconsumer.web;

import com.example.annotation.RpcReference;
import org.example.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Web {

    @RpcReference
    HelloService helloService;

    @GetMapping
    public Object hello(String arg){
        return helloService.hello(arg);
    }
}
