package org.example.socket.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.common.RpcFuture;
import org.example.common.RpcRequestHolder;
import org.example.socket.codec.RpcProtocol;
import org.example.socket.codec.RpcResponse;

public class ClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    /**
     * 当有消息到达时被调用
     * @param channelHandlerContext
     * @param rpcResponseRpcProtocol
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                RpcProtocol<RpcResponse> rpcResponseRpcProtocol) throws Exception {
        long requestId = rpcResponseRpcProtocol.getHeader().getRequestId();
        RpcFuture<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        future.getPromise().setSuccess(rpcResponseRpcProtocol.getBody());
    }
}
