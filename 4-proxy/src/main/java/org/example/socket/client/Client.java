package org.example.socket.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.common.Cache;
import org.example.common.ServiceName;
import org.example.common.URL;
import org.example.common.constants.RpcProxy;
import org.example.proxy.IProxy;
import org.example.proxy.ProxyFactory;
import org.example.service.HelloService;
import org.example.socket.codec.RpcDecoder;
import org.example.socket.codec.RpcEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    private Logger logger = LoggerFactory.getLogger(Client.class);

    private final String host;

    private final Integer port;

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    private ChannelFuture channelFuture;

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;

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

    }

    public void registerBean(String serviceName) {
        final URL url = new URL(host, port);
        Cache.services.put(new ServiceName(serviceName), url);
        channelFuture = bootstrap.connect(host, port);
        Cache.channelFutureMap.put(url, channelFuture);
    }

    public static void main(String[] args) throws Exception {
        final Client client = new Client("127.0.0.1", 8081);
        client.registerBean(HelloService.class.getName());
        final IProxy iProxy = ProxyFactory.get(RpcProxy.CG_LIB);
        final HelloService proxy = iProxy.getProxy(HelloService.class);
        System.out.println(proxy.hello("xixi"));
    }
}
