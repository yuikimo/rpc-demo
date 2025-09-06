package org.example.common.constants;

public enum FaultTolerant {

    // 故障转移策略
    Failover("failover"),
    // 快速失败策略
    FailFast("failFast");

    public String name;

    FaultTolerant(String name) {
        this.name = name;
    }
}
