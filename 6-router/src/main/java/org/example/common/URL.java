package org.example.common;

import java.util.Objects;

public class URL {

    private String ip;
    private Integer port;
    private String serviceName;
    private String version;

    public URL() {
    }

    public URL(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        URL url = (URL) object;
        return Objects.equals(ip, url.ip) && Objects.equals(port, url.port) && Objects.equals(
                serviceName, url.serviceName) && Objects.equals(version, url.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port, serviceName, version);
    }

    @Override
    public String toString() {
        return "URL{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", serviceName='" + serviceName + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
