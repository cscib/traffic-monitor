package com.traffic.trafficmonitor.drones.services;

import com.traffic.trafficmonitor.dispatcher.repositories.DroneMonitorPointRepository;
import com.traffic.trafficmonitor.drones.DroneUtils;
import com.traffic.trafficmonitor.model.cache.DroneMonitorPoint;
import com.traffic.trafficmonitor.model.cache.TrafficReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DroneService {

    @Value("${drones.averageSpeedInMetresPerSecond}")
    private double averageSpeed;

    @Value("${drones.startTimeMs}")
    private long startTimeMs;

    @Autowired
    private TrafficReporterService trafficReporterService;

    @Autowired
    private DroneMonitorPointRepository droneMonitorPointRepository;

    private final long realStartTimeMs = OffsetDateTime.now().toInstant().toEpochMilli();

    public void simulateFlight(long droneId, long flightTimeMs) {
        try {

            log.info("[{}] Flight Simulation of drone. Waiting for {} seconds.", droneId, flightTimeMs);
            Thread.sleep(flightTimeMs);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void simulateFlight(DroneMonitorPoint moveToPoint, long flightTimeMs) {
        try {

            log.debug("[{}] Flight Simulation of drone to point {},{} expected to take {} seconds.",
                    moveToPoint.getDroneId(),
                    moveToPoint.getLatitude(),
                    moveToPoint.getLongitude(),
                    flightTimeMs/1000);
            Thread.sleep(flightTimeMs);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void reportTrafficConditions(DroneMonitorPoint moveToPoint) {
        Optional.ofNullable(moveToPoint.getStationNames())
                .filter(stations -> !stations.isEmpty())
                .map(stations -> buildTrafficReport(moveToPoint.getDroneId(), stations))
                .ifPresent(trafficReporterService::report);
    }

    private OffsetDateTime calculateSimulatedTime(){
        if (startTimeMs <= 0) return OffsetDateTime.now();

        long timeDifferenceInMs = realStartTimeMs - startTimeMs;
        //.atZoneSameInstant((ZoneId.of("GMT"))).
        return OffsetDateTime.now().minusSeconds(timeDifferenceInMs/1000);
    }

    private TrafficReport buildTrafficReport(int droneId, List<String> stations) {

        return TrafficReport.builder()
                .droneId(droneId)
                .droneSpeed(averageSpeed)
                .stationsNearby(stations)
                .trafficConditions(DroneUtils.getTrafficConditions().getValue())
                .time(calculateSimulatedTime())
                .build();
    }



}
