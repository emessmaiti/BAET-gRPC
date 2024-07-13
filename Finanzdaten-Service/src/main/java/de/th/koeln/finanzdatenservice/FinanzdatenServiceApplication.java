package de.th.koeln.finanzdatenservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FinanzdatenServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinanzdatenServiceApplication.class, args);
    }

}
