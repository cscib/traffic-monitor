package com.traffic.trafficmonitor.drones;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;



@Component
@Slf4j
public class DroneManager {

    @Value("#{${rabbitmq.queues.map}}")
    private Map<String, Long> queuesMap;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    private Collection<Thread> droneThreadCollection;

    @PostConstruct
    public void initializeDrones() throws ExceptionInInitializerError {
        droneThreadCollection = Collections.synchronizedCollection(new ArrayList<>());

        for (Map.Entry<String, Long> queueEntry : queuesMap.entrySet()) {
            Drone drone = new Drone(queueEntry.getKey(), queueEntry.getValue());
            beanFactory.initializeBean(drone, queueEntry.getKey());
            beanFactory.autowireBean(drone);
            Thread thread = new Thread(drone);
            droneThreadCollection.add(thread);
            thread.start();
            log.info("[{}] Created bean {}", queueEntry.getValue(), drone.getClass().getName());
        }
    }

    public boolean shutDown(){
        droneThreadCollection.forEach(drone->drone.interrupt());
        return true;
    }
}

