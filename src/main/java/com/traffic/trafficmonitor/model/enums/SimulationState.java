package com.traffic.trafficmonitor.model.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.traffic.trafficmonitor.commons.SimulationStateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonDeserialize(using = SimulationStateDeserializer.class)
public enum SimulationState implements Serializable {


    START(0, "start"),
    STOP(1, "stop");

    @Getter
    private int index;

    @Getter
    private String state;
//
//    @JsonValue
//    public String getState(){
//        return state;
//    }
}

