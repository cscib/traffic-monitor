package com.traffic.trafficmonitor.model.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class FlyPoint {

    @Id
    private int droneId;
    private double latitude;
    private double longitude;
    private String timestamp;
    private List<Station> stations;
}
