package com.traffic.trafficmonitor.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TrafficConditions {


    HEAVY(0, "HEAVY"),
    LIGHT(2, "LIGHT"),
    MODERATE (1, "MODERATE");

    @Getter
    private final int index;

    @Getter
    private final String value;
}
