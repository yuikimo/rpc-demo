package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    // 指定实现类，默认为实现接口中第一个
    Class<?> serviceInterface() default void.class;

    // 版本
    String version() default "1.0";
}
