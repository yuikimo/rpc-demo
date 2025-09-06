package org.example.proxy.cglib;

import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.example.annotation.RpcReference;
import org.example.common.Cache;
import org.example.common.Host;
import org.example.common.RpcFuture;
import org.example.common.RpcRequestHolder;
import org.example.common.URL;
import org.example.common.constants.FaultTolerant;
import org.example.common.constants.LoadBalance;
import org.example.common.constants.MsgType;
import org.example.common.constants.ProtocolConstants;
import org.example.common.constants.Register;
import org.example.common.constants.RpcSerialization;
import org.example.filter.Filter;
import org.example.filter.FilterData;
import org.example.filter.FilterFactory;
import org.example.filter.FilterLoader;
import org.example.filter.FilterResponse;
import org.example.register.RegistryFactory;
import org.example.router.LoadBalancer;
import org.example.router.LoadBalancerFactory;
import org.example.socket.codec.MsgHeader;
import org.example.socket.codec.RpcProtocol;
import org.example.socket.codec.RpcRequest;
import org.example.socket.codec.RpcResponse;
import org.example.tolerant.FaultContext;
import org.example.tolerant.FaultTolerantFactory;
import org.example.tolerant.FaultTolerantStrategy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CgLibProxy implements MethodInterceptor {

    private final String serviceName;
    private final String version;
    private final FaultTolerant faultTolerant;
    private final long time;
    private final TimeUnit timeUnit;

    public CgLibProxy(Class clazz) {
        this.serviceName = clazz.getName();

        final RpcReference rpcService = (RpcReference) clazz.getAnnotation(RpcReference.class);
        version = rpcService.version();
        faultTolerant = rpcService.faultTolerant();
        time = rpcService.time();
        timeUnit = rpcService.timeUnit();
    }

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
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodCode(method.hashCode());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setServiceVersion(version);

        if (null != objects && objects.length > 0) {
            rpcRequest.setParameterTypes(objects[0].getClass());
            rpcRequest.setParameter(objects[0]);
        }

        rpcProtocol.setBody(rpcRequest);

        final List<URL> urls = RegistryFactory.get(Register.ZOOKEEPER).discoveries(serviceName, version);
        if (urls.isEmpty()) {
            throw new Exception("无服务可用:" + serviceName);
        }

        final LoadBalancer loadBalancer = LoadBalancerFactory.get(LoadBalance.Round);
        final URL url = loadBalancer.select(urls);

        final ChannelFuture channelFuture = Cache.CHANNEL_FUTURE_MAP.get(new Host(url.getIp(), url.getPort()));

        final List<Filter> clientBeforeFilters = FilterFactory.getClientBeforeFilters();
        if (!clientBeforeFilters.isEmpty()) {
            final FilterData<RpcRequest> rpcRequestFilterData = new FilterData<>(rpcRequest);
            final FilterLoader filterLoader = new FilterLoader();
            filterLoader.addFilter(clientBeforeFilters);
            final FilterResponse filterResponse = filterLoader.doFilter(rpcRequestFilterData);
            if (!filterResponse.getResult()) {
                throw filterResponse.getException();
            }
        }

        // 发送
        channelFuture.channel().writeAndFlush(rpcProtocol);

        RpcFuture<RpcResponse> future = new RpcFuture(new DefaultPromise(new DefaultEventLoop()), time);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);
        RpcResponse rpcResponse = future.getPromise().sync().get(future.getTimeout(), timeUnit);

        final List<Filter> clientAfterFilters = FilterFactory.getClientAfterFilters();
        if (!clientBeforeFilters.isEmpty()) {
            final FilterData<RpcResponse> rpcResponseFilterData = new FilterData<>(rpcResponse);
            final FilterLoader filterLoader = new FilterLoader();
            filterLoader.addFilter(clientAfterFilters);
            final FilterResponse filterResponse = filterLoader.doFilter(rpcResponseFilterData);
            if (!filterResponse.getResult()) {
                throw filterResponse.getException();
            }
        }
        // 发生异常
        if (rpcResponse.getException() != null) {
            rpcResponse.getException().printStackTrace();
            final FaultContext faultContext = new FaultContext(url, urls, rpcProtocol, requestId,
                                                               rpcResponse.getException());
            final FaultTolerantStrategy faultTolerantStrategy = FaultTolerantFactory.get(faultTolerant);
            return faultTolerantStrategy.handler(faultContext);
        }
        return rpcResponse.getData();
    }
}
