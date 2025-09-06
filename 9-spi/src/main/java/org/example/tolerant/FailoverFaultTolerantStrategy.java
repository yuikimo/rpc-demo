package org.example.tolerant;

import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import org.example.common.Cache;
import org.example.common.Host;
import org.example.common.RpcFuture;
import org.example.common.RpcRequestHolder;
import org.example.common.URL;
import org.example.socket.codec.RpcResponse;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FailoverFaultTolerantStrategy implements FaultTolerantStrategy {

    /**
     * 从注册的URL列表中移除当前请求URL,并将列表中的第一个URL返回
     * @param faultContext
     * @return
     * @throws Exception
     */
    @Override
    public Object handler(FaultContext faultContext) throws Exception {
        final URL currentURL = faultContext.getCurrentURL();
        final List<URL> urls = faultContext.getUrls();
        final Iterator<URL> iterator = urls.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(currentURL)) {
                iterator.remove();
            }
        }

        if (urls.isEmpty()) {
            throw new Exception("服务端发生异常,触发故障容错机制: 故障转移,无服务可用");
        }
        final URL url = urls.get(0);
        // 获取对应的缓存连接结果
        final ChannelFuture channelFuture = Cache.CHANNEL_FUTURE_MAP.get(new Host(url.getIp(), url.getPort()));
        // 发送自定义请求协议
        channelFuture.channel().writeAndFlush(faultContext.getRpcProtocol());

        // 异步等待响应结果
        RpcFuture<RpcResponse> future = new RpcFuture(new DefaultPromise(new DefaultEventLoop()), 3000);
        RpcRequestHolder.REQUEST_MAP.put(faultContext.getRequestId(), future);
        RpcResponse rpcResponse = future.getPromise().sync().get(future.getTimeout(), TimeUnit.MILLISECONDS);
        // 如果仍然发送异常，进行进行故障转移，直到无服务可用
        if (rpcResponse.getException() != null) {
            faultContext.setCurrentURL(url);
            return handler(faultContext);
        }
        return rpcResponse.getData();
    }
}
