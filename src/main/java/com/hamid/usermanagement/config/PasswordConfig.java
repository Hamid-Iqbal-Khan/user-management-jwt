package com.hamid.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordConfig
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * Passwords must NEVER be stored in plain text.
 *
 * If passwords are stored as plain text:
 * - Database leaks expose all user passwords
 * - Users are at serious security risk
 * - Application fails basic security standards
 *
 * This configuration class provides a secure way
 * to HASH passwords before storing them in the database
 * and VERIFY them during login.
 *
 * Spring Security needs a PasswordEncoder bean
 * to perform password encryption and comparison.
 */
@Configuration
public class PasswordConfig {

    /**
     * Creates a PasswordEncoder bean.
     *
     * WHY:
     * ----
     * - Spring Security looks for a PasswordEncoder bean
     * - Used to encode passwords during registration
     * - Used to match passwords during login
     *
     * WHY BCrypt:
     * ------------
     * - Strong hashing algorithm
     * - Automatically handles salt generation
     * - Resistant to brute-force attacks
     * - Industry standard for password hashing
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        // BCryptPasswordEncoder hashes passwords using BCrypt algorithm
        // Example:
        // plain password  -> $2a$10$XyZ...
        return new BCryptPasswordEncoder();
    }
}
