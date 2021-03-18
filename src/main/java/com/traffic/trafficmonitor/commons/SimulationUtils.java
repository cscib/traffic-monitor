package com.traffic.trafficmonitor.commons;

import java.time.OffsetDateTime;

public class SimulationUtils {

    public static OffsetDateTime calculateSimulatedTime(long startTimeMs, long realStartTimeMs){
        if (startTimeMs <= 0) return OffsetDateTime.now();

        // current time - fake time in logs
        long timeDifferenceInMs = realStartTimeMs - startTimeMs;
        //.atZoneSameInstant((ZoneId.of("GMT"))).
        return OffsetDateTime.now().minusSeconds(timeDifferenceInMs/1000);
    }

}
