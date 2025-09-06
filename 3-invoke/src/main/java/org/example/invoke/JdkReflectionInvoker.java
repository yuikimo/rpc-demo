package org.example.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JdkReflectionInvoker implements Invoker {

    // Key: 方法的哈希值 Value: 调用实现
    private Map<Integer, MethodInvocation> methodCache = new HashMap<>();

    @Override
    public Object invoke(Invocation invocation) throws InvocationTargetException, IllegalAccessException,
                                                       ClassNotFoundException, NoSuchMethodException,
                                                       InstantiationException {
        // 获取方法的哈希值看是否缓存过
        final Integer methodCode = invocation.getMethodCode();
        if (!methodCache.containsKey(methodCode)) {
            final Class<?> aClass = Class.forName(invocation.getClassName());
            final Method method = aClass.getMethod(invocation.getMethodName(), invocation.getParameterTypes());

            methodCache.put(methodCode, new MethodInvocation(aClass.newInstance(), method));
        }
        final MethodInvocation methodInvocation = methodCache.get(methodCode);
        return methodInvocation.invoke(invocation.getParameter());
    }
}
