package com.hamid.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginRequest
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * This DTO represents the REQUEST sent by the client
 * when a user tries to log in.
 *
 * It acts as a CONTRACT between client and server.
 *
 * RESPONSIBILITIES:
 * -----------------
 * - Hold login credentials
 * - Enforce validation rules
 * - Prevent invalid data from reaching service layer
 *
 * Using a DTO keeps the controller clean
 * and avoids exposing internal entity structures.
 */
@Data                   // Generates getters, setters, equals, hashCode, toString
@AllArgsConstructor     // Generates constructor with all fields
@NoArgsConstructor      // Required for JSON deserialization
public class LoginRequest {

    /**
     * User email address.
     *
     * VALIDATION:
     * -----------
     * @Email    -> Ensures valid email format (example@domain.com)
     * @NotBlank -> Prevents null, empty, or whitespace values
     *
     * WHY:
     * ----
     * - Email is used as unique user identifier
     * - Invalid emails should never reach authentication logic
     */
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    /**
     * User password.
     *
     * VALIDATION:
     * -----------
     * @NotBlank -> Prevents empty or null password
     *
     * WHY:
     * ----
     * - Password must be provided to authenticate user
     * - Actual password validation happens in service layer
     */
    @NotBlank(message = "Password is mandatory")
    private String password;
}
