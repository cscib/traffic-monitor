package com.traffic.trafficmonitor.commons;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.traffic.trafficmonitor.model.enums.SimulationState;

import java.io.IOException;
import java.util.stream.Stream;

public class SimulationStateDeserializer extends JsonDeserializer<SimulationState> {
    @Override
    public SimulationState deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String state = node.get("state").asText();
        return Stream.of(SimulationState.values())
                .filter(enumValue -> enumValue.getState().equals(state))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("state "+state+" is not recognized"));
    }
}

