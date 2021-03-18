package com.traffic.trafficmonitor.dispatcher;

import com.traffic.trafficmonitor.dispatcher.repositories.DroneMonitorPointRepository;
import com.traffic.trafficmonitor.dispatcher.repositories.TrafficReportRepository;
import com.traffic.trafficmonitor.model.cache.DroneMonitorPoint;
import com.traffic.trafficmonitor.model.cache.TrafficReport;
import com.traffic.trafficmonitor.model.dto.Station;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvRoutines;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class DataLoader{

    @Value("${drones.maxStationDistanceInMetres}")
    private int distanceInMetres;

    @Value("${redis.stations.key}")
    private String stationsKey;

    @Value("${data.directory}")
    private String directory;

    @Value("${station.fileName}")
    private String stationFileName;

    @Value("${drones.fileName}")
    private String[] pointsFileNames;

    @Autowired
    private DroneMonitorPointRepository droneMonitorPointRepository;

    @Autowired
    private TrafficReportRepository trafficReportRepository;

    @Autowired
    private CsvParserSettings csvParserSettings;

    @Autowired
    private RedisTemplate redisTemplate;


    public void load(){

        // clear cache before starting
        trafficReportRepository.deleteAll();
        droneMonitorPointRepository.deleteAll();

        // load stations to repository
        final String stationPathToFile = directory + stationFileName;

        log.info("Reading Stations from {} and Storing to Repository.",stationPathToFile);
        Path path = Paths.get(stationPathToFile);
        List<Station> stations =  read(path, Station.class);
        saveGeoLocations(stations);

        // load points to repository
        String pointsPathToFile;
        List<DroneMonitorPoint> points = new ArrayList<>();

        for (String pointsFileName: pointsFileNames) {
            pointsPathToFile = directory + pointsFileName;

            log.info("Loading Point from {} and Storing to Repository.",pointsPathToFile);
            Path pointsPath = Paths.get(pointsPathToFile);
            points.addAll(readPointsAndAddNearbyStations(pointsPath));
        }
        droneMonitorPointRepository.saveAll(points);
    }


    private <T> List<T> read(Path path, Class<T> beanType) {
        return new CsvRoutines(csvParserSettings).parseAll(beanType, path.toFile(), "UTF-8");
    }

    private List<DroneMonitorPoint> readPointsAndAddNearbyStations(Path pointsPath) {
        List<DroneMonitorPoint> points = new ArrayList<>();
        for (DroneMonitorPoint droneMonitorPoint : read(pointsPath, DroneMonitorPoint.class)){
            List<String> stationNames = findStationsWithinDistanceFrom(new Point(droneMonitorPoint.getLatitude(), droneMonitorPoint.getLongitude()));
            droneMonitorPoint.setStationNames(stationNames);
            points.add(droneMonitorPoint);
        }
        return points;
    }

    private void saveGeoLocations(List<Station> stations) {
        if (redisTemplate.hasKey(stationsKey)) {
            return;
        }

        for (Station station : stations) {
            Point point = new Point(station.getLatitude(), station.getLongitude());
            redisTemplate.opsForGeo().add(stationsKey, point, station.getStation());
        }
    }

    private List<String> findStationsWithinDistanceFrom(Point point) {

        Distance distance = new Distance(distanceInMetres, RedisGeoCommands.DistanceUnit.METERS);
        Circle circle = new Circle(point, distance);
        GeoResults results = redisTemplate.opsForGeo()
                .radius(stationsKey, circle);

        Iterator resultsIterator = results.iterator();
        List<String> list = new ArrayList<>();
        while (resultsIterator.hasNext()) {
            GeoResult geoResult = (GeoResult) resultsIterator.next();
            RedisGeoCommands.GeoLocation content = (RedisGeoCommands.GeoLocation) geoResult.getContent();
            list.add(String.valueOf(content.getName()));
        }
        return list;
    }




}
