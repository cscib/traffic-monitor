package com.traffic.trafficmonitor.dispatcher.repositories;

import com.traffic.trafficmonitor.model.cache.TrafficReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficReportRepository extends CrudRepository<TrafficReport, String> {

}
