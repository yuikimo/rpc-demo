package org.example.filter.client;

import org.example.filter.Filter;
import org.example.socket.codec.RpcRequest;

public interface ClientBeforeFilter extends Filter<RpcRequest> {
}

