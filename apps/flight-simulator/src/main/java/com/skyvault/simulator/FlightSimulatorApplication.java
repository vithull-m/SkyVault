package com.skyvault.simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FlightSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightSimulatorApplication.class, args);
    }
}
