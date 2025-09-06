package org.example.common;

import java.util.Objects;

public class ServiceName {

    private final String name;

    public ServiceName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ServiceName{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ServiceName that = (ServiceName) object;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
