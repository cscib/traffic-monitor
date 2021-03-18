package com.traffic.trafficmonitor.dispatcher.controllers;

import com.traffic.trafficmonitor.dispatcher.DispatcherManager;
import com.traffic.trafficmonitor.dispatcher.services.TrafficService;
import com.traffic.trafficmonitor.model.cache.TrafficReport;
import com.traffic.trafficmonitor.model.enums.SimulationState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
class TrafficController {

    @Autowired
    private TrafficService trafficService;

    @Autowired
    private DispatcherManager dispatcherManager;

    @PostMapping(value = "/v1/drones/createTrafficReport", consumes = MediaType.APPLICATION_JSON_VALUE,
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

    @PutMapping(value ="/v1/drones/simulation", consumes=MediaType.APPLICATION_JSON_VALUE)
    public void setSimulationState(@RequestBody SimulationState simulationState) {
        log.info("Setting simulation to state:{}", simulationState.getState());

        if (simulationState.getState().equals("stop")) {
            dispatcherManager.stop();
            log.warn("Simulation was stopped");
        } else {
            // no logic yet set in place to restart once stopped.
        }

    }
}