package de.th.koeln.benutzerdatenservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class BenutzerdatenServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BenutzerdatenServiceApplication.class, args);
    }

}
