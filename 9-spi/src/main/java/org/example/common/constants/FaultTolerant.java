package org.example.common.constants;

public enum FaultTolerant {
    Failover("failover"),

    FailFast("failFast");


    public String name;
    FaultTolerant(String type){
        this.name = type;
    }


}
