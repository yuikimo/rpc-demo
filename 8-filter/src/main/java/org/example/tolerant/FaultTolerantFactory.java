package org.example.tolerant;

import org.example.common.constants.FaultTolerant;

import java.util.HashMap;
import java.util.Map;

public class FaultTolerantFactory {

    private static Map<FaultTolerant, FaultTolerantStrategy> faultTolerantStrategyMap = new HashMap<>();

    static {
        faultTolerantStrategyMap.put(FaultTolerant.Failover, new FailoverFaultTolerantStrategy());
    }

    public static FaultTolerantStrategy get(FaultTolerant faultTolerant) {
        return faultTolerantStrategyMap.get(faultTolerant);
    }
}

