package org.example.tolerant;

import org.example.common.URL;
import org.example.socket.codec.RpcProtocol;

import java.util.List;

public class FaultContext {

    // 当前请求的URL
    private URL currentURL;
    // 已注册的URL
    private List<URL> urls;
    // 请求协议
    private RpcProtocol rpcProtocol;

    private Long requestId;

    private Exception exception;

    public FaultContext() {
    }

    public FaultContext(URL currentURL, List<URL> urls, RpcProtocol rpcProtocol, Long requestId, Exception e) {
        this.currentURL = currentURL;
        this.urls = urls;
        this.rpcProtocol = rpcProtocol;
        this.requestId = requestId;
        this.exception = e;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public URL getCurrentURL() {
        return currentURL;
    }

    public void setCurrentURL(URL currentURL) {
        this.currentURL = currentURL;
    }

    public List<URL> getUrls() {
        return urls;
    }

    public void setUrls(List<URL> urls) {
        this.urls = urls;
    }

    public RpcProtocol getRpcProtocol() {
        return rpcProtocol;
    }

    public void setRpcProtocol(RpcProtocol rpcProtocol) {
        this.rpcProtocol = rpcProtocol;
    }
}
