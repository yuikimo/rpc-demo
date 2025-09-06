package org.example.socket.serialization;

import org.example.spi.ExtensionLoader;

import java.io.IOException;

public class SerializationFactory {

    public static RpcSerialization get(org.example.common.constants.RpcSerialization serialization) {
        return ExtensionLoader.getInstance().get(serialization.name);
    }

    public static RpcSerialization get(String name) {
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(RpcSerialization.class);
    }
}
