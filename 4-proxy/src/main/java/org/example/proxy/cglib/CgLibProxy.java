package org.example.proxy.cglib;

import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.example.common.Cache;
import org.example.common.RpcFuture;
import org.example.common.RpcRequestHolder;
import org.example.common.ServiceName;
import org.example.common.URL;
import org.example.common.constants.MsgType;
import org.example.common.constants.ProtocolConstants;
import org.example.common.constants.RpcSerialization;
import org.example.socket.codec.MsgHeader;
import org.example.socket.codec.RpcProtocol;
import org.example.socket.codec.RpcRequest;
import org.example.socket.codec.RpcResponse;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class CgLibProxy implements MethodInterceptor {

    private final Object object;

    public CgLibProxy(Object o) {
        this.object = o;
    }

    @Override
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
        rpcRequest.setClassName(object.getClass().getName());
        rpcRequest.setMethodCode(method.hashCode());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setServiceVersion("1.0");

        if (null != objects && objects.length > 0) {
            rpcRequest.setParameterTypes(objects[0].getClass());
            rpcRequest.setParameter(objects[0]);
        }

        rpcProtocol.setBody(rpcRequest);

        final URL url = Cache.services.get(new ServiceName(object.getClass().getName()));
        final ChannelFuture channelFuture = Cache.channelFutureMap.get(url);
        channelFuture.channel().writeAndFlush(rpcProtocol);

        RpcFuture<RpcResponse> future = new RpcFuture(new DefaultPromise(new DefaultEventLoop()), 3000);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);
        RpcResponse rpcResponse = future.getPromise().sync().get(future.getTimeout(), TimeUnit.MILLISECONDS);

        if (rpcResponse.getException() != null) {
            throw rpcResponse.getException();
        }

        return rpcResponse.getData();
    }
}
