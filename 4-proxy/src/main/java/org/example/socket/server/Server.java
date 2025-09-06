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
        EventLoopGroup bossGroup = new NioEventLoopGroup();
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
                                   socketChannel.pipeline().addLast(new ServerHandler());
                               }
                           })
                           .option(ChannelOption.SO_BACKLOG, 128)
                           .childOption(ChannelOption.SO_KEEPALIVE, true);

            serverBootstrap.bind(port).channel().closeFuture().sync();
            logger.info("rpc server 启动: ", port);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new Server(8081).run();
    }
}
