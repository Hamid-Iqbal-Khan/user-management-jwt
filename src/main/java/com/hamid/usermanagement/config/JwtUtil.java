package com.hamid.usermanagement.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * JwtUtil
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * JWT tokens are just encoded strings.
 * Spring Security does NOT know:
 *  - how to create them
 *  - how to validate them
 *  - how to extract user data from them
 *
 * This utility class CENTRALIZES all JWT-related logic.
 *
 * RESPONSIBILITIES:
 * -----------------
 * 1. Generate JWT token after successful login
 * 2. Validate incoming JWT token
 * 3. Extract user identity (email) from JWT
 *
 * Without this class:
 * -------------------
 * - Token generation logic would be duplicated
 * - Token validation would be inconsistent
 * - Security code would become messy and error-prone
 */
@Component
public class JwtUtil {

    /**
     * Secret key used to SIGN and VERIFY JWT tokens.
     *
     * WHY:
     * ----
     * - Prevents token tampering
     * - Only server knows this secret
     * - If token is modified, signature validation fails
     *
     * NOTE:
     * -----
     * In real applications, move this to application.properties
     */
    private static final String SECRET_KEY =
            "my-super-secret-key-my-super-secret-key";

    /**
     * Cryptographic key created from SECRET_KEY.
     *
     * WHY:
     * ----
     * - JWT library requires a Key object
     * - HS256 algorithm needs a 256-bit key
     * - Converts String secret into secure signing key
     */
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // =====================================================
    // GENERATE TOKEN
    // =====================================================

    /**
     * Generates a JWT token for an authenticated user.
     *
     * WHEN IS THIS CALLED:
     * -------------------
     * - After successful login
     *
     * WHAT IT DOES:
     * -------------
     * - Stores user identity (email) inside token
     * - Adds issue time and expiration time
     * - Digitally signs token using secret key
     *
     * @param email authenticated user's email
     * @return signed JWT token
     */
    public String generateToken(String email) {

        return Jwts.builder()                 // Start building JWT
                .subject(email)               // Store user identity
                .issuedAt(new Date())         // Token creation time
                .expiration(                 // Token expiry time
                        new Date(
                                System.currentTimeMillis() + 86400000
                        )
                )
                .signWith(key)                // Sign token using secret key
                .compact();                   // Convert to String
    }

    // =====================================================
    // EXTRACT EMAIL FROM TOKEN
    // =====================================================

    /**
     * Extracts user email from JWT token.
     *
     * WHY:
     * ----
     * - We need to know who is making the request
     * - Email is used as user identity (principal)
     *
     * @param token JWT token
     * @return email stored inside token
     */
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // =====================================================
    // VALIDATE TOKEN
    // =====================================================

    /**
     * Validates JWT token.
     *
     * WHAT IT CHECKS:
     * ---------------
     * - Token signature is valid
     * - Token is not expired
     * - Token structure is correct
     *
     * WHY:
     * ----
     * - Prevents use of fake or expired tokens
     * - Protects secured APIs
     *
     * @param token JWT token
     * @return true if token is valid, false otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            extractClaims(token); // Will throw exception if token is invalid
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    // =====================================================
    // INTERNAL: EXTRACT CLAIMS
    // =====================================================

    /**
     * Extracts claims (payload) from JWT token.
     *
     * CLAIMS contain:
     * ---------------
     * - subject (email)
     * - issuedAt
     * - expiration
     *
     * WHY THIS METHOD EXISTS:
     * -----------------------
     * - Avoid duplicate parsing logic
     * - Central place for JWT parsing
     *
     * @param token JWT token
     * @return Claims object
     */
    private Claims extractClaims(String token) {

        return Jwts.parser()                       // Create JWT parser
                .verifyWith((javax.crypto.SecretKey) key) // Verify signature
                .build()
                .parseSignedClaims(token)          // Parse token
                .getPayload();                     // Extract claims
    }
}
