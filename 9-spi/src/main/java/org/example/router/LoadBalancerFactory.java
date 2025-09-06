package org.example.router;

import org.example.common.constants.LoadBalance;
import org.example.spi.ExtensionLoader;

import java.io.IOException;

public class LoadBalancerFactory {


    public static LoadBalancer get(LoadBalance loadBalance) {
        return ExtensionLoader.getInstance().get(loadBalance.name);
    }

    public static LoadBalancer get(String name) {
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(LoadBalancer.class);
    }
}
