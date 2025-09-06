package com.example.socket.server;

import com.example.config.Properties;
import com.example.filter.FilterFactory;
import com.example.invoke.InvokerFactory;
import com.example.register.RegistryFactory;
import com.example.socket.codec.RpcDecoder;
import com.example.socket.codec.RpcEncoder;
import com.example.socket.serialization.SerializationFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    private Integer port;

    private ServerBootstrap bootstrap;

    public Server(Integer port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap= new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new RpcEncoder());
                            ch.pipeline().addLast(new RpcDecoder());
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            if (port == null){
                bootstrap.bind(0).addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            Channel channel = channelFuture.channel();
                            InetSocketAddress localAddress = (InetSocketAddress) channel.localAddress();
                            Properties.setPort(localAddress.getPort());
                        }
                    }
                }).sync().channel().closeFuture().sync();
            }else{
                bootstrap.bind(port).sync().channel().closeFuture().sync();
            }
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void init() throws IOException, ClassNotFoundException {
        RegistryFactory.init();
        FilterFactory.initServer();
        InvokerFactory.init();
        SerializationFactory.init();
    }

}
