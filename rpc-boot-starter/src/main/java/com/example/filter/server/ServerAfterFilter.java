package com.example.filter.server;

import com.example.filter.Filter;
import com.example.socket.codec.RpcResponse;

public interface ServerAfterFilter extends Filter<RpcResponse> {
}
