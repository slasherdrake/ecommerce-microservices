package com.ecommerce.orderservice.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ServiceAuthFilter implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, 
                                      ClientHttpRequestExecution execution) throws IOException {
        
        System.out.println("ServiceAuthFilter interceptor called for: " + request.getURI());
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof JwtAuthenticationToken jwtAuth) {
            String tokenValue = jwtAuth.getToken().getTokenValue();
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue);
            System.out.println("Forwarding JWT token to: " + request.getURI());
        } else {
            System.out.println("No JWT authentication found, auth: " + auth);
        }
        
        return execution.execute(request, body);
    }
}