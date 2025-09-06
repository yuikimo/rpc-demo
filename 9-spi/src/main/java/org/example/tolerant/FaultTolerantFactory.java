package org.example.tolerant;

import org.example.common.constants.FaultTolerant;
import org.example.spi.ExtensionLoader;

import java.io.IOException;

public class FaultTolerantFactory {

    public static FaultTolerantStrategy get(FaultTolerant faultTolerant){
        return ExtensionLoader.getInstance().get(faultTolerant.name);
    }

    public static FaultTolerantStrategy get(String name){
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        final ExtensionLoader extensionLoader = ExtensionLoader.getInstance();
        extensionLoader.loadExtension(FaultTolerantStrategy.class);
    }
}
