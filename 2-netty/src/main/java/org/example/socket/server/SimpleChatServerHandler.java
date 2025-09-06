package org.example.socket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.socket.codec.RpcProtocol;

public class SimpleChatServerHandler extends SimpleChannelInboundHandler<RpcProtocol> {

    // 当接收到RpcProtocol消息时被调用
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol rpcRequestRpcProtocol) throws Exception {
        System.out.println(rpcRequestRpcProtocol.getBody());
    }
}
