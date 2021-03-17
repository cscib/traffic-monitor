package com.traffic.trafficmonitor.model.cache;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
@ToString
@RedisHash("traffic_report")
public class TrafficReport {

    @Id
    private String id;
    @Indexed
    private final long droneId;

    //2011-03-22T09:06:35.561+01:00
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSSx")
    private final OffsetDateTime time;
    private final double droneSpeed;
    private final String trafficConditions;
    private final List<String> stationsNearby;
}
