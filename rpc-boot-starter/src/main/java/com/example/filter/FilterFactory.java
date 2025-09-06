package com.example.filter;

import com.example.filter.client.ClientAfterFilter;
import com.example.filter.client.ClientBeforeFilter;
import com.example.filter.server.ServerAfterFilter;
import com.example.filter.server.ServerBeforeFilter;
import com.example.spi.ExtensionLoader;

import java.io.IOException;
import java.util.List;

public class FilterFactory {

    public static List<Filter> getClientBeforeFilters() {
        return ExtensionLoader.getInstance().gets(ClientBeforeFilter.class);

    }

    public static List<Filter> getClientAfterFilters() {
        return ExtensionLoader.getInstance().gets(ClientAfterFilter.class);

    }

    public static List<Filter> getServerBeforeFilters() {
        return ExtensionLoader.getInstance().gets(ServerBeforeFilter.class);

    }

    public static List<Filter> getServerAfterFilters() {
        return ExtensionLoader.getInstance().gets(ServerAfterFilter.class);
    }


    public static void initClient() throws IOException, ClassNotFoundException {
        final ExtensionLoader extensionLoader = ExtensionLoader.getInstance();
        extensionLoader.loadExtension(ClientAfterFilter.class);
        extensionLoader.loadExtension(ClientBeforeFilter.class);
    }

    public static void initServer() throws IOException, ClassNotFoundException {
        final ExtensionLoader extensionLoader = ExtensionLoader.getInstance();
        extensionLoader.loadExtension(ServerAfterFilter.class);
        extensionLoader.loadExtension(ServerBeforeFilter.class);
    }
}
