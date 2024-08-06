package de.th.koeln.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hauptklasse für die Gateway-Service-Anwendung.
 *
 * <p>Diese Klasse startet die Spring Boot-Anwendung und aktiviert die Service-Discovery für den Gateway.
 * Die Konfiguration des Gateway-Services erfolgt in der Datei <code>application.yml</code>.</p>
 */
@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

}
