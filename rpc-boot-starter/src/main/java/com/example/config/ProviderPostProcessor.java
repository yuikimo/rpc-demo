package com.example.config;

import com.example.annotation.RpcService;
import com.example.common.Cache;
import com.example.common.URL;
import com.example.filter.FilterFactory;
import com.example.invoke.InvokerFactory;
import com.example.register.RegistryFactory;
import com.example.register.RegistryService;
import com.example.socket.serialization.SerializationFactory;
import com.example.socket.server.Server;
import com.example.utils.IpUtil;
import com.example.utils.ServiceNameBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class ProviderPostProcessor implements InitializingBean, BeanPostProcessor {

    private RpcProperties rpcProperties;

    private final String ip = IpUtil.getIP();

    public ProviderPostProcessor(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        RegistryFactory.init();
        FilterFactory.initServer();
        InvokerFactory.init();
        SerializationFactory.init();
        Thread t = new Thread(() -> {
            final Server server = new Server(rpcProperties.getPort());
            try {
                server.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 找到bean上带有 RpcService 注解的类
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            // 可能会有多个接口,默认选择第一个接口
            String serviceName = beanClass.getInterfaces()[0].getName();
            // 是否指定了实现接口
            if (!rpcService.serviceInterface().equals(void.class)) {
                serviceName = rpcService.serviceInterface().getName();
            }

            try {
                // 注册服务
                RegistryService registryService = RegistryFactory.get(rpcProperties.getRegistry().getName());

                final URL url = new URL();
                url.setPort(Properties.getPort());
                url.setIp(ip);
                url.setServiceName(serviceName);
                url.setVersion(rpcService.version());
                registryService.register(url);

                // 缓存
                final String key = ServiceNameBuilder.buildServiceKey(serviceName, rpcService.version());
                Cache.SERVICE_MAP.put(key, bean);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return bean;
    }
}
