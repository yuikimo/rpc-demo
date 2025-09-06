package org.example.socket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.common.constants.MsgType;
import org.example.common.constants.RpcInvoker;
import org.example.filter.Filter;
import org.example.filter.FilterData;
import org.example.filter.FilterFactory;
import org.example.filter.FilterLoader;
import org.example.filter.FilterResponse;
import org.example.invoke.Invocation;
import org.example.invoke.Invoker;
import org.example.invoke.InvokerFactory;
import org.example.socket.codec.MsgHeader;
import org.example.socket.codec.RpcProtocol;
import org.example.socket.codec.RpcRequest;
import org.example.socket.codec.RpcResponse;

import java.util.List;

public class ServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcProtocol) throws
                                                                                                                  Exception {

        final MsgHeader header = rpcProtocol.getHeader();
        final RpcRequest rpcRequest = rpcProtocol.getBody();

        final RpcResponse response = new RpcResponse();
        final RpcProtocol<RpcResponse> resRpcProtocol = new RpcProtocol();

        header.setMsgType((byte) MsgType.RESPONSE.ordinal());
        resRpcProtocol.setHeader(header);

        final Invoker invoker = InvokerFactory.get(RpcInvoker.JDK);
        try {
            final List<Filter> serverBeforeFilters = FilterFactory.getServerBeforeFilters();
            if (!serverBeforeFilters.isEmpty()) {
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

        } catch (Exception e) {
            response.setException(e);
        } finally {
            final List<Filter> serverAfterFilters = FilterFactory.getServerAfterFilters();
            if (!serverAfterFilters.isEmpty()) {
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
