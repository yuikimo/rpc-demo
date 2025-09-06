package com.example.socket.server;

import com.example.filter.FilterData;
import com.example.filter.FilterResponse;
import com.example.filter.server.ServerBeforeFilter;
import com.example.socket.codec.RpcRequest;

public class ServerTokenFilter implements ServerBeforeFilter {
    @Override
    public FilterResponse doFilter(FilterData<RpcRequest> filterData) {
        final RpcRequest rpcRequest = filterData.getObject();
        Object value = rpcRequest.getClientAttachments().get("token");
        if (!value.equals("xhy")){
            return new FilterResponse(false,new Exception("token 不正确,当前token为:" + value));
        }
        return new FilterResponse(true,null);
    }
}
