package com.example.filter.server;

import com.example.filter.Filter;
import com.example.socket.codec.RpcRequest;

public interface ServerBeforeFilter extends Filter<RpcRequest> {


}
