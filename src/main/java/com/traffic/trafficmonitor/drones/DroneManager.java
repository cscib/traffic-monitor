package com.traffic.trafficmonitor.drones;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Map;



@Component
@Slf4j
public class DroneManager {

    @Value("#{${rabbitmq.queues.map}}")
    private Map<String, Long> queuesMap;

    @Autowired
    AutowireCapableBeanFactory beanFactory;

    @PostConstruct
    public void initializeDrones() throws ExceptionInInitializerError {

        for (Map.Entry<String, Long> queueEntry : queuesMap.entrySet()) {
            Drone drone = new Drone(queueEntry.getKey(), queueEntry.getValue());
            beanFactory.initializeBean(drone, queueEntry.getKey());
            beanFactory.autowireBean(drone);
            Thread thread = new Thread(drone);
            thread.start();
            log.info("[{}] Created bean {}", queueEntry.getValue(), drone.getClass().getName());
        }
    }
}

