package com.example.router;

import com.example.common.URL;

import java.util.List;

public interface LoadBalancer {

    URL select(List<URL> urls);

}
