package org.example.service;

import org.example.annotation.RpcReference;

@RpcReference
public interface IHelloService {

    Object hello(String text);
}
