package org.example.socket.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class SimpleChatClientHandler extends ChannelInboundHandlerAdapter {

    // 在连接建立成功后被调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = Unpooled.copiedBuffer("Hello Server", CharsetUtil.UTF_8);
        ctx.writeAndFlush(message);
    }

    // 在客户端接收到来自服务器的数据时调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inBuffer = (ByteBuf) msg;
        System.out.println("Client received: " + inBuffer.toString(CharsetUtil.UTF_8));
    }
}
