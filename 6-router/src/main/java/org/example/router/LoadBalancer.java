package org.example.router;

import org.example.common.URL;

import java.util.List;

public interface LoadBalancer {

    URL select(List<URL> urls);
}
