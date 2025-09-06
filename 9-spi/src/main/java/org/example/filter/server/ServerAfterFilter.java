package org.example.filter.server;

import org.example.filter.Filter;
import org.example.socket.codec.RpcResponse;

public interface ServerAfterFilter extends Filter<RpcResponse> {
}
