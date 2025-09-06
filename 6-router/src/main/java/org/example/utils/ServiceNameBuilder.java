package org.example.utils;

public class ServiceNameBuilder {

    public static String buildServiceKey(String serviceName, String serviceVersion) {
        return String.join("$", serviceName, serviceVersion);
    }

    public static String buildServiceNodeInfo(String key, String ip, Integer port) {
        return String.join("#", key, ip, String.valueOf(port));
    }
}
