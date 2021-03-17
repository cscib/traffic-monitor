package com.traffic.trafficmonitor.dispatcher.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traffic.trafficmonitor.dispatcher.repositories.DroneMonitorPointRepository;
import com.traffic.trafficmonitor.model.cache.DroneMonitorPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@EnableAsync
@Component
public class DispatcherService {

    @Value("${rabbitmq.topicExchangeName}")
    private String topicExchangeName;

    @Value("${rabbitmq.queues.routingKey.prefix}")
    private String routingKeyPrefix;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DroneMonitorPointRepository droneMonitorPointRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Async
    public void sendMessagesToDrone(Long droneId, Iterable<DroneMonitorPoint> points) {
            log.info("[{}] Starting to send messages to drone.", droneId);
            List<DroneMonitorPoint> droneMonitorPointList =
                    StreamSupport.stream(points.spliterator(), false)
                            .filter(point -> point.getDroneId()==droneId)
                            .collect(Collectors.toList());
            String routingKey = routingKeyPrefix + droneId;
            for (DroneMonitorPoint point : droneMonitorPointList) {
                try {
                    log.debug("[{}] Sending message {} {} {} {}", point.getDroneId(), point.getLatitude(), point.getLongitude(), point.getTimestamp(), point.getStationNames());
                    rabbitTemplate.convertAndSend(topicExchangeName, routingKey, buildMessage(point));
                } catch (JsonProcessingException e) {
                    log.error("Error sending queue for drone {} with message.", droneId);
                }
            }
    }

    public Message buildMessage(DroneMonitorPoint point) throws JsonProcessingException {

            String orderJson = objectMapper.writeValueAsString(point);
            return MessageBuilder
                    .withBody(orderJson.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .build();

    }

}
