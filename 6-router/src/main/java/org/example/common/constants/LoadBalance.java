package org.example.common.constants;

/**
 * 负载均衡策略
 */
public enum LoadBalance {
    Round("round");

    public String name;

    LoadBalance(String name) {
        this.name = name;
    }
}
