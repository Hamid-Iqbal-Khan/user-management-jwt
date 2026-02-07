package com.hamid.usermanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RegisterRequest
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * This DTO represents the REQUEST sent by the client
 * when a new user wants to register.
 *
 * It defines the exact data the server expects
 * and enforces validation rules before business logic runs.
 *
 * BENEFITS:
 * ---------
 * - Prevents invalid data from reaching service layer
 * - Keeps controller clean
 * - Protects entity from direct exposure
 * - Improves API reliability and security
 */
@Data                   // Generates getters, setters, equals, hashCode, toString
@AllArgsConstructor     // Generates constructor with all fields
@NoArgsConstructor      // Required for JSON deserialization
public class RegisterRequest {

    /**
     * User's full name.
     *
     * VALIDATION:
     * -----------
     * @NotBlank -> Ensures name is not null, empty, or whitespace
     *
     * WHY:
     * ----
     * - Name is mandatory during registration
     * - Empty names should never be stored
     */
    @NotBlank(message = "Name is mandatory")
    private String name;

    /**
     * User's email address.
     *
     * VALIDATION:
     * -----------
     * @Email    -> Ensures email format is valid
     * @NotBlank -> Ensures email is provided
     *
     * WHY:
     * ----
     * - Email is used as unique identifier
     * - Invalid email breaks login & communication
     */
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is mandatory")
    private String email;

    /**
     * User's password.
     *
     * VALIDATION:
     * -----------
     * @Size(min = 6) -> Ensures minimum password strength
     *
     * WHY:
     * ----
     * - Very short passwords are insecure
     * - Actual password encryption happens in service layer
     *
     * NOTE:
     * -----
     * @NotBlank is NOT required here because:
     * - @Size(min = 6) already prevents empty values
     */
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
