package com.example.filter.client;

import com.example.filter.Filter;
import com.example.socket.codec.RpcResponse;

public interface ClientAfterFilter extends Filter<RpcResponse> {
}
