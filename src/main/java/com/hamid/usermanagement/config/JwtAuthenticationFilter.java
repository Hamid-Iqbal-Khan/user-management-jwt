package com.hamid.usermanagement.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JwtAuthenticationFilter
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED (MOST IMPORTANT PART)
 * =========================================================
 *
 * Spring Security does NOT understand JWT tokens by default.
 *
 * Even if the client sends a valid JWT token in the request:
 *   Authorization: Bearer <token>
 *
 * Spring Security will STILL treat the request as ANONYMOUS
 * unless we explicitly tell it:
 *   - how to read the token
 *   - how to validate the token
 *   - how to extract user information
 *   - how to mark the request as authenticated
 *
 * This class does EXACTLY that.
 *
 * Without this filter:
 * --------------------
 * - JWT token is ignored
 * - SecurityContext remains empty
 * - User is considered anonymous
 * - Protected APIs (DELETE, UPDATE) return 403 Forbidden
 *
 * With this filter:
 * -----------------
 * - JWT token is read and validated
 * - User identity is extracted
 * - Authentication is stored in SecurityContext
 * - Spring Security allows access to protected APIs
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JwtUtil handles token validation and extraction
    private final JwtUtil jwtUtil;

    // Constructor injection ensures JwtUtil is available
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * This method runs ONCE for EVERY incoming HTTP request.
     *
     * PURPOSE:
     * --------
     * Convert a JWT token into a Spring Security Authentication object.
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // STEP 1: Read the Authorization header from request
        // Client sends token like:
        // Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
        String authHeader = request.getHeader("Authorization");

        // STEP 2: Check if JWT is present and correctly formatted
        // WHY:
        // - Avoid NullPointerException
        // - Ignore requests without JWT
        // - Ignore malformed headers
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            // STEP 3: Extract JWT token by removing "Bearer "
            String token = authHeader.substring(7);

            // STEP 4: Validate the token
            // This ensures:
            // - Token signature is valid
            // - Token is not expired
            // - Token is not tampered
            if (jwtUtil.isTokenValid(token)) {

                // STEP 5: Extract user identity (email) from token
                // This was stored as "subject" during token generation
                String email = jwtUtil.extractEmail(token);

                // STEP 6: Create Authentication object
                // This tells Spring Security:
                // "This request belongs to an authenticated user"
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,                  // principal (who the user is)
                                null,                   // credentials (not needed here)
                                Collections.emptyList() // authorities (roles added later)
                        );

                // STEP 7: Attach request metadata
                // (IP address, session info, etc.)
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // STEP 8: Store authentication in SecurityContext
                // THIS LINE IS WHY THIS CLASS EXISTS
                // After this, Spring Security knows the user is authenticated
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        // STEP 9: Continue filter chain
        // If we do not call this, request processing stops here
        filterChain.doFilter(request, response);
    }
}
