package com.example.apisample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiSampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiSampleApplication.class, args);
    }

}
