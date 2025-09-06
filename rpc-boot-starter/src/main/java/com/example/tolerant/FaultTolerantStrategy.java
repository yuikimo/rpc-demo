package com.example.tolerant;

public interface FaultTolerantStrategy {

    Object handler(FaultContext faultContext) throws Exception;
}
