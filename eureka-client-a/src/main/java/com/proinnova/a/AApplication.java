package com.proinnova.a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AApplication {

    public static void main(String[] args) {
        SpringApplication.run(AApplication.class, args);
    }
}
