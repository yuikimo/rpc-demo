package com.example.filter;

public class FilterData<T> {

    private T object;

    public FilterData(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }
}
