package org.example.proxy.cglib;

import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.example.common.Cache;
import org.example.common.Host;
import org.example.common.RpcFuture;
import org.example.common.RpcRequestHolder;
import org.example.common.URL;
import org.example.common.constants.MsgType;
import org.example.common.constants.ProtocolConstants;
import org.example.common.constants.Register;
import org.example.common.constants.RpcSerialization;
import org.example.register.RegistryFactory;
import org.example.service.HelloService;
import org.example.socket.codec.MsgHeader;
import org.example.socket.codec.RpcProtocol;
import org.example.socket.codec.RpcRequest;
import org.example.socket.codec.RpcResponse;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CgLibProxy implements MethodInterceptor {

    private final Object object;

    private final String serviceName;

    public CgLibProxy(Object o) {
        this.object = o;
        this.serviceName = o.getClass().getName();
    }

    /**
     * 拦截调用方法，封装RpcProtocol对象
     */
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        final RpcProtocol rpcProtocol = new RpcProtocol();
        // 构建消息头
        MsgHeader header = new MsgHeader();
        long requestId = RpcRequestHolder.getRequestId();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(requestId);

        final byte[] serialization = RpcSerialization.JSON.name.getBytes();
        header.setSerializationLen(serialization.length);
        header.setSerialization(serialization);
        header.setMsgType((byte) MsgType.REQUEST.ordinal());
        header.setStatus((byte) 0x1);
        rpcProtocol.setHeader(header);

        final RpcRequest rpcRequest = new RpcRequest();
        final Class<HelloService> aClass = HelloService.class;
        rpcRequest.setClassName(aClass.getName());
        rpcRequest.setMethodCode(method.hashCode());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setServiceVersion("1.0");

        if (null != objects && objects.length > 0) {
            rpcRequest.setParameterTypes(objects[0].getClass());
            rpcRequest.setParameter(objects[0]);
        }

        rpcProtocol.setBody(rpcRequest);

        // 查询注册中心里是否注册了该方法，如果有返回第一个(后续改为以轮询的方式)
        final List<URL> urls = RegistryFactory.get(Register.ZOOKEEPER).discoveries(serviceName, "1.0");
        if (urls.isEmpty()) {
            throw new Exception("无服务可用:" + serviceName);
        }
        final URL url = urls.get(0);

        final ChannelFuture channelFuture = Cache.CHANNEL_FUTURE_MAP.get(new Host(url.getIp(), url.getPort()));
        channelFuture.channel().writeAndFlush(rpcProtocol);

        RpcFuture<RpcResponse> future = new RpcFuture(new DefaultPromise(new DefaultEventLoop()), 3000);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);

        // 等待调用返回结果
        RpcResponse rpcResponse = future.getPromise().sync().get(future.getTimeout(), TimeUnit.MILLISECONDS);
        if (rpcResponse.getException() != null) {
            throw rpcResponse.getException();
        }
        return rpcResponse.getData();
    }
}
