package org.example.register;

import org.example.common.constants.Register;

import java.util.HashMap;
import java.util.Map;

public class RegistryFactory {

    private static Map<Register, RegistryService> registerServiceMap = new HashMap<>();

    static {
        registerServiceMap.put(Register.ZOOKEEPER, new CuratorZookeeperRegistry("127.0.0.1:2181"));
    }

    public static RegistryService get(Register register) {
        return registerServiceMap.get(register);
    }
}
