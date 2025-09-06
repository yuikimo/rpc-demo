package org.example.event;

import org.example.common.Cache;
import org.example.common.Host;
import org.example.common.ServiceName;
import org.example.common.URL;

public class DestroyRpcLister implements IRpcLister<DestroyEventData>{
    @Override
    public void exec(DestroyEventData destroyEventData) {
        final URL url = (URL) destroyEventData.getData();
        final ServiceName serviceName = new ServiceName(url.getServiceName(), url.getVersion());
        // 将服务从缓存中移除
        if (Cache.SERVICE_URLS.containsKey(serviceName)){
            Cache.SERVICE_URLS.get(serviceName).remove(url);
        }
        // 移除缓存连接
        final Host ip = new Host(url.getIp(), url.getPort());
        if (Cache.CHANNEL_FUTURE_MAP.containsKey(ip)) {
            Cache.CHANNEL_FUTURE_MAP.remove(ip);
        }
    }
}
