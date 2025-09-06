package com.example.rpcconsumer.filter;

import com.example.filter.FilterData;
import com.example.filter.FilterResponse;
import com.example.filter.client.ClientBeforeFilter;
import com.example.socket.codec.RpcRequest;
public class TokenFilter implements ClientBeforeFilter {
    @Override
    public FilterResponse doFilter(FilterData<RpcRequest> filterData) {
        final RpcRequest rpcRequest = filterData.getObject();
        rpcRequest.getClientAttachments().put("token","xhy123");
        return new FilterResponse(true,null);
    }
}
