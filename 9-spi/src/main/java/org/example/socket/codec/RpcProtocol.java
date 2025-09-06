package org.example.socket.codec;

import java.io.Serializable;

/**
 * 自定义请求协议
 * @param <T>
 */
public class RpcProtocol<T> implements Serializable {

    // 请求头
    private MsgHeader header;
    // 请求体
    private T body;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
