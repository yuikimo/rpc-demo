package com.example.tolerant;

import com.example.common.constants.FaultTolerant;
import com.example.spi.ExtensionLoader;

import java.io.IOException;

public class FaultTolerantFactory {


    public static FaultTolerantStrategy get(FaultTolerant faultTolerant){
        final String name = faultTolerant.name;
        return ExtensionLoader.getInstance().get(name);
    }

    public static FaultTolerantStrategy get(String name){
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        final ExtensionLoader extensionLoader = ExtensionLoader.getInstance();
        extensionLoader.loadExtension(FaultTolerantStrategy.class);
    }
}
