package org.example.invoke;

import org.example.socket.codec.RpcRequest;

/**
 * 对 RpcRequest 进行语义化封装
 */
public class Invocation {

    private String serviceVersion;
    private String className;
    private String methodName;
    private Integer methodCode;

    private Object parameter;
    private Class<?> parameterTypes;

    public Invocation(RpcRequest rpcRequest) {
        this.serviceVersion = rpcRequest.getServiceVersion();
        this.className = rpcRequest.getClassName();
        this.methodName = rpcRequest.getMethodName();
        this.parameter = rpcRequest.getParameter();
        this.parameterTypes = rpcRequest.getParameterTypes();
        this.methodCode = rpcRequest.getMethodCode();
    }

    public Invocation() {}

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(Integer methodCode) {
        this.methodCode = methodCode;
    }

    public Object getParameter() {
        return parameter;
    }

    public void setParameter(Object parameter) {
        this.parameter = parameter;
    }

    public Class<?> getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?> parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
}
