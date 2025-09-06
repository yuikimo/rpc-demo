package org.example.event;

import io.netty.channel.ChannelFuture;
import org.example.common.Cache;
import org.example.common.Host;
import org.example.common.ServiceName;
import org.example.common.URL;

import java.util.ArrayList;

public class AddRpcLister implements IRpcLister<AddRpcEventData>{

    @Override
    public void exec(AddRpcEventData addRpcEventData) {
        final URL url = (URL) addRpcEventData.getData();
        final ServiceName serviceName = new ServiceName(url.getServiceName(), url.getVersion());

        // 如果缓存中没有
        if (!Cache.SERVICE_URLS.containsKey(serviceName)) {
            Cache.SERVICE_URLS.put(serviceName, new ArrayList<>());
        }

        Cache.SERVICE_URLS.get(serviceName).add(url);

        final Host ip = new Host(url.getIp(), url.getPort());
        if (!Cache.CHANNEL_FUTURE_MAP.containsKey(ip)) {
            ChannelFuture channelFuture = Cache.BOOT_STRAP.connect(url.getIp(), url.getPort());
            Cache.CHANNEL_FUTURE_MAP.put(ip, channelFuture);
        }
    }
}
