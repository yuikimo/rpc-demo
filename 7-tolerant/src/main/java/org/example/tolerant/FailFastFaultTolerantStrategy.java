package org.example.tolerant;

/**
 * 快速失败
 */
public class FailFastFaultTolerantStrategy implements FaultTolerantStrategy {

    @Override
    public Object handler(FaultContext faultContext) throws Exception {
        return faultContext.getException();
    }
}
