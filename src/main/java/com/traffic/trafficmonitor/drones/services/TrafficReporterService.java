package com.traffic.trafficmonitor.drones.services;

import com.traffic.trafficmonitor.dispatcher.services.TrafficService;
import com.traffic.trafficmonitor.model.cache.TrafficReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class TrafficReporterService {

    @Autowired
    private TrafficService trafficService;

    public void report(TrafficReport trafficReport) {
        log.info("[{}] Time: {} Speed: {} Traffic Conditions: {} Stations: {}",
                trafficReport.getDroneId(),
                trafficReport.getTime(),
                trafficReport.getDroneSpeed(),
                trafficReport.getTrafficConditions(),
                trafficReport.getStationsNearby().stream().map(s -> s.toString()).collect(Collectors.joining(",")));

        trafficService.saveTrafficReport(trafficReport);
    }
}
