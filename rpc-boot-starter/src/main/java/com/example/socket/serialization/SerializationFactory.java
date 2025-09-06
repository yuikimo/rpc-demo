package com.example.socket.serialization;

import com.example.spi.ExtensionLoader;
import com.example.common.constants.RpcSerialization;

import java.io.IOException;

public class SerializationFactory {
    public static com.example.socket.serialization.RpcSerialization get(RpcSerialization serialization){
        return ExtensionLoader.getInstance().get(serialization.name);
    }

    public static com.example.socket.serialization.RpcSerialization get(String name){
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(com.example.socket.serialization.RpcSerialization.class);
    }
}
