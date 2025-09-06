package com.example.rpcprovider.filter;

import com.example.filter.FilterData;
import com.example.filter.FilterResponse;
import com.example.filter.server.ServerBeforeFilter;
import com.example.socket.codec.RpcRequest;

public class TokenFilter implements ServerBeforeFilter {

    @Override
    public FilterResponse doFilter(FilterData<RpcRequest> filterData) {
        final RpcRequest rpcRequest = filterData.getObject();
        Object token = rpcRequest.getClientAttachments().get("token");
        if (!token.equals("xhy")){
            return new FilterResponse(false,new Exception("token不正确"));
        }
        return new FilterResponse(true, null);
    }
}
