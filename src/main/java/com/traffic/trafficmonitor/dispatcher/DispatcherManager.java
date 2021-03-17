package com.traffic.trafficmonitor.dispatcher;

import com.traffic.trafficmonitor.dispatcher.repositories.DroneMonitorPointRepository;
import com.traffic.trafficmonitor.dispatcher.services.DispatcherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component
public class DispatcherManager implements ApplicationRunner {


    @Value("#{${rabbitmq.queues.map}}")
    private Map<String, Long> queuesMap;

    @Autowired
    private DroneMonitorPointRepository droneMonitorPointRepository;

    @Autowired
    private DispatcherService dispatcherService;

    @Autowired
    private DataLoader dataLoader;


    @Override
    public void run(ApplicationArguments args){
        dataLoader.load();
        sendPointsToDrones();
    }

    private void sendPointsToDrones() {
        for (Long droneId: queuesMap.values()) {
            dispatcherService.sendMessagesToDrone(droneId, droneMonitorPointRepository.findAll());
        }
    }
}
