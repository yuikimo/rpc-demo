package org.example.common;

import java.util.Objects;

public class Host {

    private final String ip;
    private final Integer port;

    public Host(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Host host = (Host) object;
        return Objects.equals(ip, host.ip) && Objects.equals(port, host.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }

    @Override
    public String toString() {
        return "Host{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
