package org.example.socket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.common.RpcFuture;
import org.example.common.RpcRequestHolder;
import org.example.socket.codec.RpcProtocol;
import org.example.socket.codec.RpcResponse;

public class ClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        long requestId = rpcResponseRpcProtocol.getHeader().getRequestId();
        RpcFuture<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        // 通知 Promise 进行回调
        future.getPromise().setSuccess(rpcResponseRpcProtocol.getBody());
    }
}
