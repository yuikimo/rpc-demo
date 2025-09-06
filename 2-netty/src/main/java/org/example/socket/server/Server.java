package org.example.socket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.example.socket.codec.RpcDecoder;
import org.example.socket.codec.RpcEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private Logger logger = LoggerFactory.getLogger(Server.class);

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // 负责监听端口，接受连接请求
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 负责处理已接受连接的IO事件（读写操作）
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                           .channel(NioServerSocketChannel.class)
                           .childHandler(new ChannelInitializer<SocketChannel>() {
                               @Override
                               protected void initChannel(SocketChannel socketChannel) throws Exception {
                                   socketChannel.pipeline().addLast(new RpcEncoder());
                                   socketChannel.pipeline().addLast(new RpcDecoder());
                                   // 处理解码后的请求
                                   socketChannel.pipeline().addLast(new SimpleChatServerHandler());
                               }
                           })
                           // 设置连接请求的队列长度
                           .option(ChannelOption.SO_BACKLOG, 128)
                           // 设置连接保持活跃
                           .childOption(ChannelOption.SO_KEEPALIVE, true);

            serverBootstrap.bind(port).sync().channel().closeFuture().sync();
            logger.info("rpc server 启动: ",port);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Server(8081).run();
    }
}
