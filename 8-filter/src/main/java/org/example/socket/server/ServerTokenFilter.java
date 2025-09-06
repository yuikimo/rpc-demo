package org.example.socket.server;

import org.example.filter.FilterData;
import org.example.filter.FilterResponse;
import org.example.filter.server.ServerBeforeFilter;
import org.example.socket.codec.RpcRequest;

public class ServerTokenFilter implements ServerBeforeFilter {

    @Override
    public FilterResponse doFilter(FilterData<RpcRequest> filterData) {
        final RpcRequest rpcRequest = filterData.getObject();
        Object value = rpcRequest.getClientAttachments().get("token");

        if (!value.equals("xhy")) {
            return new FilterResponse(false, new Exception("token 不正确，当前token为：" + value));
        }
        return new FilterResponse(true, null);
    }
}
