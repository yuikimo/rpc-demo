package org.example.socket.serialization;

import org.example.common.constants.RpcSerialization;

import java.util.HashMap;
import java.util.Map;

public class SerializationFactory {

    private static Map<RpcSerialization, org.example.socket.serialization.RpcSerialization>
            serializationMap = new HashMap<>();

    static {
        serializationMap.put(RpcSerialization.JSON, new JsonSerialization());
    }

    public static org.example.socket.serialization.RpcSerialization get(RpcSerialization serialization) {
        return serializationMap.get(serialization);
    }
}
