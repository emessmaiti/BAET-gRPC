package de.th.koeln.authentifizierungservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "de.th.koeln")
@EnableDiscoveryClient
public class AuthentifizierungServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthentifizierungServiceApplication.class, args);
	}

}
