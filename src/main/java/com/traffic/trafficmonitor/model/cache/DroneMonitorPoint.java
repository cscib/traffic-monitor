package com.traffic.trafficmonitor.model.cache;

import com.univocity.parsers.annotations.Parsed;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash("points")
public class DroneMonitorPoint {

    @Id
    private String id;
    @Parsed(field = "drone_id")
    @Indexed
    private int droneId;
    @Parsed
    private double latitude;
    @Parsed
    private double longitude;
    @Parsed
    private String timestamp;
    private List<String> stationNames;
}
