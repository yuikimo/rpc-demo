package org.example.register;

import org.example.common.constants.Register;
import org.example.spi.ExtensionLoader;

import java.io.IOException;

public class RegistryFactory {

    public static RegistryService get(Register register) {
        return ExtensionLoader.getInstance().get(register.name);
    }

    public static RegistryService get(String name) {
        return ExtensionLoader.getInstance().get(name);
    }

    public static void init() throws IOException, ClassNotFoundException {
        ExtensionLoader.getInstance().loadExtension(RegistryService.class);
    }
}
