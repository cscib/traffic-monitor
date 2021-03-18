package com.traffic.trafficmonitor.drones;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traffic.trafficmonitor.drones.services.DroneService;
import com.traffic.trafficmonitor.model.cache.DroneMonitorPoint;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Slf4j
public class Drone implements Runnable{

    @Value("${drones.averageSpeedInMetresPerSecond}")
    private double averageSpeed;

    @Value("${drones.startTimeMs}")
    private long startTimeMs;

    private ConcurrentLinkedQueue<DroneMonitorPoint> nextPointQueue = new ConcurrentLinkedQueue<DroneMonitorPoint>();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DroneService droneService;

    private boolean running = false;

    private static final ThreadLocal<String> QueueName = new ThreadLocal<String>();

    public static void setQueueName(String name) {
        QueueName.set(name);
    }

    public static String getQueueName() {
        return QueueName.get();
    }

    private String name;

    private long droneId;

    public Drone(String name, long droneId){
        Drone.setQueueName(name);
        this.name = name;
        this.droneId =droneId;
    }

    @RabbitListener(
            queues = "#{T(com.traffic.trafficmonitor.drones.Drone).getQueueName()}",
            containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(Message message) {
        log.debug(String.format("To %s Received by %s %s ", message.getMessageProperties().getConsumerQueue(),
                this.getName(), new String(message.getBody())));
        try {
            DroneMonitorPoint movePoint = objectMapper.readValue(message.getBody(), DroneMonitorPoint.class);
            long pointsInMemory = nextPointQueue.size();
            while (pointsInMemory >8) {
                try {
                    Thread.sleep(10000);
                    pointsInMemory = nextPointQueue.size();
                } catch (InterruptedException e) {
                    log.error("[{}] The drone has more than 10 points {} in memory.", getName(), nextPointQueue.size());
                }
            }
            nextPointQueue.add(movePoint);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        log.debug("[{}] Flight simulation started at {} with speed {}", droneId, startTimeMs, averageSpeed);
        running = true;
        DroneMonitorPoint moveFromPoint = null;
        DroneMonitorPoint moveToPoint = null;
        while (running && !Thread.currentThread().isInterrupted()){
            try {
                // Fetch Move-to-Point (MTP) and Calculate Distance Between Move-from-Point (MFP) to Move-to-Point (MTP)
                  moveToPoint = nextPointQueue.poll();
                  if (moveToPoint != null) {
                      if (moveFromPoint == null) {
                          droneService.simulateFlight(moveToPoint, 5000L);
                      } else {
                          droneService.simulateFlight(moveToPoint, DroneUtils.calculateFlightTime(moveFromPoint, moveToPoint, averageSpeed));
                      }
                      droneService.reportTrafficConditions(moveToPoint);
                      moveFromPoint = moveToPoint;
                  } else {
                      log.info("[{}] Flying for 10 sec... No Move-to-Points received yet.", droneId);
                      droneService.simulateFlight(droneId, 10000L);
                  }
            } catch (Exception ex) {

                if (ex.getCause() instanceof InterruptedException) {
                    break;
                }
                log.error("[{}] An exception occurred and the Drone will shut down.", droneId,ex);
            }
        }
        running = false;
        nextPointQueue.clear();

        log.info("[{}] Shutting Drone...", droneId);
    }
}
