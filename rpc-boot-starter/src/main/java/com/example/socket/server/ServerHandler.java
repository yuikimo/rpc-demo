package com.example.socket.server;

import com.example.common.constants.MsgType;
import com.example.config.Properties;
import com.example.filter.*;
import com.example.invoke.Invocation;
import com.example.invoke.Invoker;
import com.example.invoke.InvokerFactory;
import com.example.socket.codec.MsgHeader;
import com.example.socket.codec.RpcProtocol;
import com.example.socket.codec.RpcRequest;
import com.example.socket.codec.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

public class ServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcProtocol) throws Exception {

        final RpcResponse response = new RpcResponse();
        final RpcProtocol<RpcResponse> resRpcProtocol = new RpcProtocol();

        final MsgHeader header = rpcProtocol.getHeader();
        final RpcRequest rpcRequest = rpcProtocol.getBody();
        header.setMsgType((byte) MsgType.RESPONSE.ordinal());
        resRpcProtocol.setHeader(header);

        final Invoker invoker = InvokerFactory.get(Properties.getInvoke());
        try {
            final List<Filter> serverBeforeFilters = FilterFactory.getServerBeforeFilters();
            if (!serverBeforeFilters.isEmpty()){
                final FilterData<RpcRequest> rpcRequestFilterData = new FilterData<>(rpcRequest);
                final FilterLoader filterLoader = new FilterLoader();
                filterLoader.addFilter(serverBeforeFilters);
                final FilterResponse filterResponse = filterLoader.doFilter(rpcRequestFilterData);
                if (!filterResponse.getResult()) {
                    throw filterResponse.getException();
                }
            }

            // 执行
            final Object data = invoker.invoke(new Invocation(rpcRequest));
            response.setData(data);

        }catch (Exception e){
            response.setException(e);
        }finally {
            final List<Filter> serverAfterFilters = FilterFactory.getServerAfterFilters();
            if (!serverAfterFilters.isEmpty()){
                final FilterData<RpcResponse> rpcResponseFilterData = new FilterData<>(response);
                final FilterLoader filterLoader = new FilterLoader();
                filterLoader.addFilter(serverAfterFilters);
                final FilterResponse filterResponse = filterLoader.doFilter(rpcResponseFilterData);
                if (!filterResponse.getResult()) {
                    throw filterResponse.getException();
                }
            }
        }

        resRpcProtocol.setBody(response);
        channelHandlerContext.writeAndFlush(resRpcProtocol);

    }
}