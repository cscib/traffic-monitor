package com.traffic.trafficmonitor.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SimulationState {


    START(0, "START"),
    STOP(1, "STOP");

    @Getter
    private final int index;

    @Getter
    private final String state;
}
