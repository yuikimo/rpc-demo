package org.example.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvocation {

    private Object object;
    private Method method;

    public MethodInvocation(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public Object invoke(Object parameter) throws InvocationTargetException, IllegalAccessException {

        return method.invoke(object, parameter);
    }
}
