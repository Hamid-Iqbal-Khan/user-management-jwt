package com.hamid.usermanagement.service;

import com.hamid.usermanagement.dto.LoginRequest;
import com.hamid.usermanagement.dto.RegisterRequest;
import com.hamid.usermanagement.dto.UpdateUserRequest;

/**
 * UserService
 *
 * =========================================================
 * WHY THIS INTERFACE IS REQUIRED
 * =========================================================
 *
 * Service layer contains BUSINESS LOGIC of the application.
 *
 * This interface defines WHAT operations can be performed
 * related to users, without exposing HOW they are implemented.
 *
 * BENEFITS:
 * ---------
 * - Clean separation of concerns
 * - Loose coupling between controller and implementation
 * - Easy unit testing using mocks
 * - Supports multiple implementations if needed
 *
 * Controller depends on this interface,
 * not on concrete implementation.
 */
public interface UserService {

    /**
     * Registers a new user.
     *
     * BUSINESS RULES:
     * ---------------
     * - Email must be unique
     * - Password must be encrypted
     * - Default role assigned
     *
     * @param registerRequest registration details
     */
    void register(RegisterRequest registerRequest);

    /**
     * Authenticates user and returns JWT token.
     *
     * BUSINESS RULES:
     * ---------------
     * - Email must exist
     * - Password must match encrypted password
     * - JWT token generated on success
     *
     * @param loginRequest login credentials
     * @return JWT token
     */
    String login(LoginRequest loginRequest);

    /**
     * Deletes a user by ID.
     *
     * BUSINESS RULES:
     * ---------------
     * - User must exist
     * - Only authenticated users allowed
     *
     * @param id user ID
     */
    void deleteUser(int id);

    /**
     * Updates user information.
     *
     * BUSINESS RULES:
     * ---------------
     * - User must exist
     * - Only allowed fields can be updated
     *
     * @param id user ID
     * @param user updated user data
     */
    void updateUser(int id, UpdateUserRequest user);
}
