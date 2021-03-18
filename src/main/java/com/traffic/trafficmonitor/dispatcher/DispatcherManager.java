package com.traffic.trafficmonitor.dispatcher;

import com.traffic.trafficmonitor.dispatcher.repositories.DroneMonitorPointRepository;
import com.traffic.trafficmonitor.dispatcher.services.DispatcherService;
import com.traffic.trafficmonitor.drones.services.TrafficReporterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

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

    private volatile boolean isRunning;

    private volatile List<Future<Boolean>> sendMessagesTasks;



    @Override
    public void run(ApplicationArguments args){
        start();
    }


    public void start() {
        log.warn(" STARTING SIMULATION *****");
        synchronized (this) {
            if (!isRunning) {
                log.warn("STARTING SIMULATION *****");
                dataLoader.load();
                sendMessagesTasks = new ArrayList<>();
                for (Long droneId: queuesMap.values()) {
                    sendMessagesTasks.add(dispatcherService.sendMessagesToDrone(droneId, droneMonitorPointRepository.findAll()));
                }
                isRunning = true;
            } else {
                log.warn("SIMULATION IS ALREADY RUNNING *****");
            }
        }
    }

    // It will run 23 minutes after start to simulate stopping at 8.10a.m.
    @Scheduled(fixedDelay = 10000000, initialDelay = 1380000)
    public void stop() {
        log.warn("STOPPING SIMULATION *****");
        synchronized (this) {
            if (isRunning) {
                sendMessagesTasks.forEach(task->task.cancel(true));
                dispatcherService.shutDownDrones();
                isRunning = false;
            }
        }
        log.warn("SIMULATION SHUTDOWN*****");
    }

    public boolean isRunning() {
        synchronized (this) {
            return isRunning;
        }
    }
}
