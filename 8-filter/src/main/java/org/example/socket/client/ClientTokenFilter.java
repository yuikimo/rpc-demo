package org.example.socket.client;

import org.example.filter.FilterData;
import org.example.filter.FilterResponse;
import org.example.filter.client.ClientBeforeFilter;
import org.example.socket.codec.RpcRequest;

public class ClientTokenFilter implements ClientBeforeFilter {

    @Override
    public FilterResponse doFilter(FilterData<RpcRequest> filterData) {
        final RpcRequest rpcRequest = filterData.getObject();

        rpcRequest.getClientAttachments().put("token", "xhy123");
        return new FilterResponse(true, null);
    }
}
