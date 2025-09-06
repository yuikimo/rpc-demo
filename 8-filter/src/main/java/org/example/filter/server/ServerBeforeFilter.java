package org.example.filter.server;

import org.example.filter.Filter;
import org.example.socket.codec.RpcRequest;

public interface ServerBeforeFilter extends Filter<RpcRequest> {
}