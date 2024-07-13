package de.th.koeln.finanzdatenservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Konfigurationsklasse für die Sicherheitskonfiguration des Finanzdatendienstes.
 *
 * <p>Diese Klasse verwendet Spring Security, um Sicherheitskonfigurationen für die Anwendung festzulegen,
 * einschließlich der Konfiguration von OAuth2-Resource-Servern und JWT-Decodern.</p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Konfiguriert die Sicherheitsfilterkette.
     *
     * @param http Die HttpSecurity-Konfiguration.
     * @return Die konfigurierte Sicherheitsfilterkette.
     * @throws Exception Wenn bei der Konfiguration ein Fehler auftritt.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/**", "/h2-console/**", "/resources/**", "/static/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(jwt -> jwt.decoder(jwtDecoder()))
                );

        return http.build();
    }

    /**
     * Konfiguriert den JWT-Decoder.
     *
     * @return Der konfigurierte JWT-Decoder.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = "https://www.googleapis.com/oauth2/v3/certs";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    /**
     * Konfiguriert den OIDC-User-Service.
     *
     * @return Der konfigurierte OIDC-User-Service.
     */
    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }
}
