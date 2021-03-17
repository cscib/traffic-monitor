package com.traffic.trafficmonitor.dispatcher.controllers;

import com.traffic.trafficmonitor.dispatcher.services.TrafficService;
import com.traffic.trafficmonitor.model.cache.TrafficReport;
import com.traffic.trafficmonitor.model.enums.SimulationState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
class TrafficController {


    @Autowired
    private TrafficService trafficService;


    @PostMapping(value = "/v1/drones/createTrafficReport", consumes = "application/json",
            produces = "application/json")
    public void createTrafficReport(@RequestBody TrafficReport trafficReport) {
        trafficService.saveTrafficReport(trafficReport);
    }

    @GetMapping("/v1/drones/{droneId}/traffic")
    List<TrafficReport> getTrafficReport(@PathVariable Long droneId) {
        return trafficService.getTrafficReports(droneId);
    }

    @GetMapping("/v1/drones/traffic")
    List<TrafficReport> getTrafficReport() {
        return trafficService.getTrafficReports();
    }

    @PutMapping
    @RequestMapping("/v1/drones/simulation")
    public void setSimulationState(@RequestBody SimulationState simulationState) {
        log.info("Setting simulation to state:{}", simulationState.getState());

        if (simulationState.getState().equals("stop")) {

        } else {

        }
        log.info("Setting simulation to state:{}", simulationState.getState());
    }
}