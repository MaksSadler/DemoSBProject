package com.example.springinaction.demosbproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoSbProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoSbProjectApplication.class, args);
    }

}
