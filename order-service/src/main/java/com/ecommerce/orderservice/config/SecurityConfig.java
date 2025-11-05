package com.ecommerce.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/orders/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );
        
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            System.out.println("JWT Claims: " + jwt.getClaims());
            
            // Extract roles from resource_access.ecommerce.roles
            Object resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess instanceof java.util.Map) {
                java.util.Map<String, Object> resourceMap = (java.util.Map<String, Object>) resourceAccess;
                Object ecommerce = resourceMap.get("ecommerce");
                if (ecommerce instanceof java.util.Map) {
                    java.util.Map<String, Object> ecommerceMap = (java.util.Map<String, Object>) ecommerce;
                    Object roles = ecommerceMap.get("roles");
                    if (roles instanceof java.util.List) {
                        java.util.List<String> rolesList = (java.util.List<String>) roles;
                        System.out.println("Extracted roles: " + rolesList);
                        return rolesList.stream()
                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role))
                                .collect(java.util.stream.Collectors.toList());
                    }
                }
            }
            System.out.println("No roles found");
            return java.util.Collections.emptyList();
        });
        return converter;
    }

    @Bean
    public org.springframework.security.oauth2.jwt.JwtDecoder jwtDecoder() {
        return org.springframework.security.oauth2.jwt.NimbusJwtDecoder
                .withJwkSetUri("http://keycloak:8080/auth/realms/drake-realm/protocol/openid-connect/certs")
                .build();
    }
}