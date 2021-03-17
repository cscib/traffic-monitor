package com.traffic.trafficmonitor.model.dto;

import com.univocity.parsers.annotations.Parsed;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station {
    @Parsed
    private String station;
    @Parsed
    private double latitude;
    @Parsed
    private double longitude;
}
