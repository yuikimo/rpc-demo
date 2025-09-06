package org.example.common;

import java.util.Objects;

public class URL {

    private String ip;
    private Integer port;

    public URL(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        URL url = (URL) object;
        return Objects.equals(ip, url.ip) && Objects.equals(port, url.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}
