package org.example.filter.client;

import org.example.filter.Filter;
import org.example.socket.codec.RpcResponse;

public interface ClientAfterFilter extends Filter<RpcResponse> {
}
