package com.example.baitap3.config;

import com.example.baitap3.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowCredentials(true);
                    corsConfig.addAllowedOriginPattern("*"); // hoặc origin frontend cụ thể
                    corsConfig.addAllowedHeader("*");
                    corsConfig.addAllowedMethod("*");
                    return corsConfig;
                }))
                .authorizeHttpRequests(auth -> auth
                        // Login & Register
                        .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                        // Users: only ADMIN
                        .requestMatchers("/api/admin/users/**").hasRole("ADMIN")
                        // Categories: MANAGER + ADMIN
                        .requestMatchers("/api/admin/categories/**").hasAnyRole("MANAGER", "ADMIN")
                        // Cart & Orders: USER + ADMIN
                        .requestMatchers("/api/cart/**", "/api/orders/**").hasAnyRole("USER", "ADMIN")
                        // Public Products
                        .requestMatchers("/api/products/**").permitAll()
                        // All other requests must be authenticated
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
