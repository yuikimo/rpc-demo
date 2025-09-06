package org.example.tolerant;

public class FailFastFaultTolerantStrategy implements FaultTolerantStrategy{

    @Override
    public Object handler(FaultContext faultContext) throws Exception {
        return faultContext.getException();
    }
}
