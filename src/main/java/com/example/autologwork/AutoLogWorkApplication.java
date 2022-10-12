package com.example.autologwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AutoLogWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoLogWorkApplication.class, args);
    }

}
