package de.th.koeln.authentifizierungservice.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Sicherheitskonfigurationsklasse für den Authentifizierungsservice.
 * Diese Klasse definiert die Sicherheitskonfigurationen,
 * einschließlich OAuth2-Login, CORS, und JWT-Decodierung.
 */
@Configuration
@EnableWebSecurity
public class securityConfig {

    /**
     * Diese Methode konfiguriert die Sicherheitsfilterkette für HTTP-Anfragen.
     * Sie deaktiviert CSRF-Schutz, definiert Zugriffsrechte für verschiedene URL-Muster,
     * konfiguriert OAuth2-Login und -Logout, und richtet einen OAuth2-Resource-Server ein.
     *
     * @param http das HttpSecurity-Objekt zur Konfiguration
     * @return das konfigurierte SecurityFilterChain-Objekt
     * @throws Exception falls bei der Konfiguration ein Fehler auftritt
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(ar -> ar
                        .requestMatchers("/h2-console/**", "/resources/**", "/static/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .defaultSuccessUrl("/api/auth/loginSuccess", true)
                        .failureUrl("/login?error=true")
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .oidcUserService(this.oidcUserService()))
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
                .build();
    }

    /**
     * Erstellt ein ClientRegistrationRepository, das die OAuth2-Clientregistrierungen verwaltet.
     *
     * @return das ClientRegistrationRepository-Objekt.
     */
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(
                this.googleClientRegistration()
        );
    }

    /**
     * Erstellt eine Clientregistrierung für Google OAuth2.
     *
     * @return das ClientRegistration-Objekt für Google.
     */
    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId("952233804551-f6gecpvftutql0lh72gef4drupvhi6v1.apps.googleusercontent.com")
                .clientSecret("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")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:8085/login/oauth2/code/{registrationId}")
                .scope("openid", "profile", "email", "address", "phone")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }

    /**
     * Erstellt einen JwtDecoder, der JWTs unter Verwendung eines JWK-Set-URI decodiert.
     *
     * @return der JwtDecoder.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }

    /**
     * Erstellt einen OidcUserService, der zur Anpassung des OIDC-Benutzerservices verwendet werden kann.
     *
     * @return der OidcUserService.
     */
    @Bean
    public OidcUserService oidcUserService() {
        // Anpassung des OidcUserService, falls erforderlich, z.B. um zusätzliche Informationen zu extrahieren
        return new OidcUserService();
    }

    /**
     * Konfiguriert die CORS-Einstellungen.
     *
     * @return die CorsConfigurationSource.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
