package com.example.socket.client;


import com.example.common.Cache;
import com.example.common.Host;
import com.example.common.URL;
import com.example.common.constants.Register;
import com.example.event.RpcListerLoader;
import com.example.filter.FilterFactory;
import com.example.proxy.ProxyFactory;
import com.example.register.RegistryFactory;
import com.example.register.RegistryService;
import com.example.router.LoadBalancerFactory;
import com.example.socket.codec.RpcDecoder;
import com.example.socket.codec.RpcEncoder;
import com.example.tolerant.FaultTolerantFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.List;

public class Client {

    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;

    public void run() {
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup)
                 .channel(NioSocketChannel.class)
                 .option(ChannelOption.SO_KEEPALIVE, true)
                 .handler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel socketChannel) throws Exception {
                         socketChannel.pipeline()
                                      .addLast(new RpcEncoder())
                                      .addLast(new RpcDecoder())
                                      .addLast(new ClientHandler());
                     }
                 });

        Cache.BOOT_STRAP = bootstrap;
    }

    public void connectServer() throws Exception {
        for (URL url : Cache.SUBSCRIBE_SERVICE_LIST) {
            final RegistryService registryService = RegistryFactory.get(Register.ZOOKEEPER);
            final List<URL> urls = registryService.discoveries(url.getServiceName(), url.getVersion());
            if (!urls.isEmpty()) {
                for (URL u : urls) {
                    final ChannelFuture connect = bootstrap.connect(u.getIp(), u.getPort());
                    Cache.CHANNEL_FUTURE_MAP.put(new Host(u.getIp(), u.getPort()), connect);
                }
            }
        }
    }

    public void init() throws IOException, ClassNotFoundException {
        new RpcListerLoader().init();
        FaultTolerantFactory.init();
        RegistryFactory.init();
        FilterFactory.initClient();
        ProxyFactory.init();
        LoadBalancerFactory.init();
    }

}
