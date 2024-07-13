package de.th.koeln.transaktionenservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TransaktionenServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransaktionenServiceApplication.class, args);
    }

}
