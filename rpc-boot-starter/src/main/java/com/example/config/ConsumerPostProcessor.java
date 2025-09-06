package com.example.config;

import com.example.annotation.RpcReference;
import com.example.common.URL;
import com.example.common.constants.Register;
import com.example.common.constants.RpcProxy;
import com.example.event.RpcListerLoader;
import com.example.filter.FilterFactory;
import com.example.proxy.IProxy;
import com.example.proxy.ProxyFactory;
import com.example.register.RegistryFactory;
import com.example.register.RegistryService;
import com.example.router.LoadBalancerFactory;
import com.example.socket.client.Client;
import com.example.socket.serialization.SerializationFactory;
import com.example.tolerant.FaultTolerantFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class ConsumerPostProcessor implements BeanPostProcessor, InitializingBean {

    RpcProperties rpcProperties;

    public ConsumerPostProcessor(RpcProperties rpcProperties) {
        this.rpcProperties = rpcProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new RpcListerLoader().init();
        FaultTolerantFactory.init();
        RegistryFactory.init();
        FilterFactory.initClient();
        ProxyFactory.init();
        LoadBalancerFactory.init();
        SerializationFactory.init();

        final Client client = new Client();
        client.run();
    }

    // 找到带有指定注解的类，并赋值为代理对象
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获取所有字段
        final Field[] fields = bean.getClass().getDeclaredFields();
        // 遍历所有字段找到 RpcReference 注解的字段
        for (Field field : fields) {
            if (field.isAnnotationPresent(RpcReference.class)) {
                field.setAccessible(true);
                final Class<?> aClass = field.getType();
                final RpcReference rpcReference = field.getAnnotation(RpcReference.class);

                final RegistryService registryService = RegistryFactory.get(Register.ZOOKEEPER);
                Object object = null;

                try {
                    final IProxy iproxy = ProxyFactory.get(RpcProxy.CG_LIB);
                    final Object proxy = iproxy.getProxy(aClass, rpcReference);
                    // 创建代理对象
                    object = proxy;

                    final URL url = new URL();
                    url.setServiceName(aClass.getName());
                    url.setVersion(rpcReference.version());
                    registryService.subscribe(url);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    // 将代理对象设置给字段
                    field.set(bean, object);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }
        return bean;
    }
}
