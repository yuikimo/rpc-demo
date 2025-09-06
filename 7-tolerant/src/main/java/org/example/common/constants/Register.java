package org.example.common.constants;

public enum Register {
    ZOOKEEPER("zookeeper");

    public String name;

    Register(String type) {
        this.name = type;
    }
}
