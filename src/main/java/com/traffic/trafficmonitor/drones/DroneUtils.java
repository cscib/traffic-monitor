package com.traffic.trafficmonitor.drones;

import com.grum.geocalc.Coordinate;
import com.grum.geocalc.EarthCalc;
import com.grum.geocalc.Point;
import com.traffic.trafficmonitor.model.cache.DroneMonitorPoint;
import com.traffic.trafficmonitor.model.enums.TrafficConditions;

import java.util.concurrent.ThreadLocalRandom;

public class DroneUtils {

    public static TrafficConditions getTrafficConditions() {
        return TrafficConditions.values()[ThreadLocalRandom.current().nextInt(TrafficConditions.values().length)];
    }

    public static double calculateDistanceBetweenPoints(DroneMonitorPoint from, DroneMonitorPoint to) {
        return EarthCalc.gcdDistance(
                Point.at(Coordinate.fromDegrees(to.getLatitude()), Coordinate.fromDegrees(to.getLongitude())),
                Point.at(Coordinate.fromDegrees(from.getLatitude()), Coordinate.fromDegrees(from.getLongitude())));
    }

    public static long calculateFlightTime(DroneMonitorPoint from, DroneMonitorPoint to, double averageSpeed) {

        double distanceInMeters = EarthCalc.gcdDistance(
                Point.at(Coordinate.fromDegrees(to.getLatitude()), Coordinate.fromDegrees(to.getLongitude())),
                Point.at(Coordinate.fromDegrees(from.getLatitude()), Coordinate.fromDegrees(from.getLongitude())));

        return calculateFlightTime(distanceInMeters, averageSpeed);
    }

    private static long calculateFlightTime(double distance, double averageSpeed) {
        if (averageSpeed <= 0) return 0;

        return (long) ((distance / averageSpeed) * 1000);
    }


}
