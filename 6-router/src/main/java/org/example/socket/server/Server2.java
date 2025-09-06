package org.example.socket.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.example.annotation.RpcService;
import org.example.common.Cache;
import org.example.common.URL;
import org.example.common.constants.Register;
import org.example.register.RegistryFactory;
import org.example.register.RegistryService;
import org.example.service.HelloService2;
import org.example.socket.codec.RpcDecoder;
import org.example.socket.codec.RpcEncoder;
import org.example.utils.ServiceNameBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Server2 {
    private Logger logger = LoggerFactory.getLogger(Server.class);

    private String host;
    private final int port;

    private ServerBootstrap bootstrap;

    public Server2(int port) {
        this.port = port;
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        host = inetAddress.getHostAddress();
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap = new ServerBootstrap();
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

            bootstrap.bind(port).sync().channel().closeFuture().sync();
            logger.info("rpc server 启动: ", port);

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public void registerBean(Class clazz) throws Exception {
        final URL url = new URL(host, port);
        if (!clazz.isAnnotationPresent(RpcService.class)) {
            throw new Exception(clazz.getName() + "没有注解 RpcService ");
        }
        final RpcService rpcService = (RpcService) clazz.getAnnotation(RpcService.class);
        String serviceName = clazz.getInterfaces()[0].getName();
        if (!(rpcService.serviceInterface().equals(void.class))) {
            serviceName = rpcService.serviceInterface().getName();
        }
        url.setServiceName(serviceName);
        url.setVersion(rpcService.version());
        final RegistryService registryService = RegistryFactory.get(Register.ZOOKEEPER);
        registryService.register(url);
        final String key = ServiceNameBuilder.buildServiceKey(serviceName, rpcService.version());
        Cache.SERVICE_MAP.put(key, clazz.newInstance());
    }

    public static void main(String[] args) throws Exception {
        final Server2 server = new Server2(8082);
        server.registerBean(HelloService2.class);
        server.run();

    }
}

