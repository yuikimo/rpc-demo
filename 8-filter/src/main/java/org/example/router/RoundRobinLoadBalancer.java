package org.example.router;

import org.example.common.URL;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer implements LoadBalancer {

    private static AtomicInteger roundRobinId = new AtomicInteger(0);

    @Override
    public URL select(List<URL> urls) {

        roundRobinId.addAndGet(1);
        if (roundRobinId.get() == Long.MAX_VALUE) {
            roundRobinId.set(0);
        }
        return urls.get(roundRobinId.get() % urls.size());
    }
}
