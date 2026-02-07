package com.hamid.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * Spring Security, by default:
 * - Enables form-based login
 * - Enables HTTP Basic authentication
 * - Enables CSRF protection
 * - Secures ALL endpoints automatically
 *
 * But our application:
 * - Is a REST API (not a web UI)
 * - Uses JWT (stateless authentication)
 * - Does NOT use sessions or login forms
 *
 * Therefore, we MUST customize Spring Security behavior.
 *
 * This class:
 * ------------
 * 1. Disables default security mechanisms we do not need
 * 2. Defines which endpoints are public vs protected
 * 3. Integrates JWT authentication into Spring Security
 *
 * Without this class:
 * -------------------
 * - Spring Security blocks all APIs
 * - JWT tokens are ignored
 * - DELETE / UPDATE always return 403
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Custom JWT authentication filter
    // Responsible for validating JWT tokens on each request
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Constructor injection (recommended best practice)
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Defines the security rules and filter chain.
     *
     * Spring Security processes requests through a chain of filters.
     * This method customizes that chain.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // =====================================================
                // DISABLE DEFAULT SECURITY FEATURES
                // =====================================================

                // Disable CSRF protection
                // WHY:
                // - CSRF is needed for session-based authentication
                // - JWT is stateless (no session)
                // - REST APIs do not need CSRF protection
                .csrf(csrf -> csrf.disable())

                // Disable form-based login
                // WHY:
                // - We do not have login pages
                // - Authentication happens via REST (/api/auth/login)
                .formLogin(form -> form.disable())

                // Disable HTTP Basic authentication
                // WHY:
                // - We do not want browser popup authentication
                // - JWT is used instead
                .httpBasic(basic -> basic.disable())

                // =====================================================
                // AUTHORIZATION RULES
                // =====================================================

                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        // Anyone can register or login
                        .requestMatchers("/api/auth/**").permitAll()

                        // Protected endpoints
                        // Only authenticated users can access user APIs
                        .requestMatchers("/api/users/**").authenticated()

                        // Any other request also requires authentication
                        .anyRequest().authenticated()
                )

                // =====================================================
                // ADD JWT AUTHENTICATION FILTER
                // =====================================================

                // This tells Spring Security:
                // "Before checking username/password authentication,
                //  first check JWT token"
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        // Build and return the security filter chain
        return http.build();
    }
}
