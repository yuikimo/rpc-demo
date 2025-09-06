package org.example.common;

import io.netty.channel.ChannelFuture;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    public static Map<ServiceName, URL> services = new HashMap<>();

    public static Map<URL, ChannelFuture> channelFutureMap = new HashMap<>();
}
