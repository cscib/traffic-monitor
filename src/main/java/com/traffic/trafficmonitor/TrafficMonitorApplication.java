package com.traffic.trafficmonitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TrafficMonitorApplication {


	public static void main(String[] args) {

		SpringApplication.run(TrafficMonitorApplication.class, args);
		log.info("DRONE DISPATCHER APPLICATION WAS STARTED");
	}

}
