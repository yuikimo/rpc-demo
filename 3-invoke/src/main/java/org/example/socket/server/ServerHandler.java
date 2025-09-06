package org.example.socket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.common.constants.MsgType;
import org.example.common.constants.RpcInvoker;
import org.example.invoke.Invocation;
import org.example.invoke.Invoker;
import org.example.invoke.InvokerFactory;
import org.example.socket.codec.MsgHeader;
import org.example.socket.codec.RpcProtocol;
import org.example.socket.codec.RpcRequest;
import org.example.socket.codec.RpcResponse;

/**
 * 处理指定类型的消息
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    /**
     * 当有消息到达时被调用
     * @param channelHandlerContext
     * @param rpcRequestRpcProtocol
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                RpcProtocol<RpcRequest> rpcRequestRpcProtocol) throws Exception {
        final MsgHeader header = rpcRequestRpcProtocol.getHeader();
        final RpcRequest rpcRequest = rpcRequestRpcProtocol.getBody();

        final RpcResponse response = new RpcResponse();
        final RpcProtocol<RpcResponse> resRpcProtocol = new RpcProtocol();

        header.setMsgType((byte) MsgType.RESPONSE.ordinal());
        resRpcProtocol.setHeader(header);

        final Invoker invoker = InvokerFactory.get(RpcInvoker.JDK);

        try {
            final Object data = invoker.invoke(new Invocation(rpcRequest));
            response.setData(data);
        } catch (Exception e) {
            response.setException(e);
        }

        resRpcProtocol.setBody(response);
        channelHandlerContext.writeAndFlush(resRpcProtocol);
    }
}
