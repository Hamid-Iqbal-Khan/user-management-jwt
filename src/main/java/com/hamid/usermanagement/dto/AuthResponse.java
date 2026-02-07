package com.hamid.usermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthResponse
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * After successful login, the server needs to return
 * authentication information to the client.
 *
 * Instead of returning raw strings or exposing internal objects,
 * we use a DTO (Data Transfer Object) to structure the response.
 *
 * This class represents the RESPONSE sent back to the client
 * after successful authentication.
 *
 * RESPONSIBILITY:
 * ---------------
 * - Hold JWT token
 * - Provide a clean and consistent API response
 *
 * WHY NOT RETURN STRING DIRECTLY:
 * -------------------------------
 * - Allows future expansion (refreshToken, expiry, user info)
 * - Keeps API responses structured
 * - Follows REST best practices
 */
@Data                   // Generates getters, setters, toString, equals, hashCode
@AllArgsConstructor     // Generates constructor with all fields
@NoArgsConstructor      // Generates default no-arg constructor
public class AuthResponse {

    /**
     * JWT token issued after successful login.
     *
     * CLIENT USE:
     * -----------
     * - Client must store this token
     * - Send it in Authorization header:
     *
     *   Authorization: Bearer <token>
     */
    private String token;
}
