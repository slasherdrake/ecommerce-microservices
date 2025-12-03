package com.ecommerce.productservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class ServiceAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String serviceRole = request.getHeader("X-Service-Role");
        String authHeader = request.getHeader("Authorization");
        
        System.out.println("ServiceAuthFilter - Service Role: " + serviceRole + ", Auth: " + authHeader);
        
        if ("ORDER_SERVICE".equals(serviceRole) && "Bearer service-token".equals(authHeader)) {
            System.out.println("Setting ORDER_SERVICE authentication");
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_ORDER_SERVICE"));
            var authentication = new UsernamePasswordAuthenticationToken("order-service", null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
}