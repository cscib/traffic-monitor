package com.traffic.trafficmonitor.dispatcher.services;

import com.traffic.trafficmonitor.dispatcher.repositories.TrafficReportRepository;
import com.traffic.trafficmonitor.model.cache.DroneMonitorPoint;
import com.traffic.trafficmonitor.model.cache.TrafficReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Slf4j
@Component
public class TrafficService {

    @Autowired
    private TrafficReportRepository trafficReportRepository;

    public void saveTrafficReport(TrafficReport trafficReport) {
        trafficReportRepository.save(trafficReport);
    }

    public List<TrafficReport> getTrafficReports(long droneId){
        List<TrafficReport> reports =
                StreamSupport.stream(trafficReportRepository.findAll()
                        .spliterator(), false)
                        .filter(report -> report.getDroneId()==droneId)
                        .collect(Collectors.toList());
        return reports;
    }

    public List<TrafficReport> getTrafficReports(){
        List<TrafficReport> reports = new ArrayList<TrafficReport>();
        trafficReportRepository.findAll()
                .forEach(reports::add);

        return reports;
    }

}
