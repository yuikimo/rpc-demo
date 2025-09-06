package com.example.filter.client;

import com.example.filter.Filter;
import com.example.socket.codec.RpcRequest;

public interface ClientBeforeFilter extends Filter<RpcRequest> {
}
