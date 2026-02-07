package com.hamid.usermanagement.controller;

import com.hamid.usermanagement.dto.AuthResponse;
import com.hamid.usermanagement.dto.LoginRequest;
import com.hamid.usermanagement.dto.RegisterRequest;
import com.hamid.usermanagement.dto.UpdateUserRequest;
import com.hamid.usermanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * This controller acts as the ENTRY POINT for all HTTP requests
 * related to user authentication and user management.
 *
 * Responsibilities:
 * -----------------
 * - Accept HTTP requests (JSON input)
 * - Validate request data using DTOs
 * - Delegate business logic to service layer
 * - Return proper HTTP responses
 *
 * IMPORTANT:
 * ----------
 * Controller MUST NOT contain business logic.
 * It should only handle request & response.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    // Service layer handles business logic
    private final UserService userService;

    // Constructor injection (best practice)
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // =====================================================
    // REGISTER USER
    // =====================================================

    /**
     * Registers a new user.
     *
     * URL:
     * ----
     * POST /api/auth/register
     *
     * WHY THIS ENDPOINT EXISTS:
     * -------------------------
     * - Allows new users to create an account
     * - Public endpoint (no authentication required)
     *
     * WHAT HAPPENS INTERNALLY:
     * ------------------------
     * - Validates request body
     * - Encrypts password
     * - Saves user to database
     *
     * @param request registration details (name, email, password)
     * @return success message
     */
    @PostMapping("/auth/register")
    public ResponseEntity<String> register(
            @Valid @RequestBody RegisterRequest request) {

        userService.register(request);

        // 201 CREATED is correct status for successful resource creation
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    // =====================================================
    // LOGIN USER
    // =====================================================

    /**
     * Authenticates user and returns JWT token.
     *
     * URL:
     * ----
     * POST /api/auth/login
     *
     * WHY THIS ENDPOINT EXISTS:
     * -------------------------
     * - Allows user to authenticate
     * - Issues JWT token for future requests
     *
     * WHAT HAPPENS INTERNALLY:
     * ------------------------
     * - Validates credentials
     * - Generates JWT token
     *
     * @param loginRequest email & password
     * @return JWT token
     */
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest) {

        // Authenticate user and generate token
        String token = userService.login(loginRequest);

        // Wrap token in response DTO
        AuthResponse response = new AuthResponse();
        response.setToken(token);

        return ResponseEntity.ok(response);
    }

    // =====================================================
    // DELETE USER
    // =====================================================

    /**
     * Deletes a user by ID.
     *
     * URL:
     * ----
     * DELETE /api/users/{id}
     *
     * WHY THIS ENDPOINT EXISTS:
     * -------------------------
     * - Allows removal of user accounts
     *
     * SECURITY:
     * ---------
     * - Requires JWT authentication
     * - Anonymous users are forbidden
     *
     * @param id user ID
     * @return success message
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {

        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    // =====================================================
    // UPDATE USER
    // =====================================================

    /**
     * Updates user information.
     *
     * URL:
     * ----
     * PUT /api/users/update/{id}
     *
     * WHY THIS ENDPOINT EXISTS:
     * -------------------------
     * - Allows users to update profile information
     * - Only selected fields are allowed to update
     *
     * SECURITY:
     * ---------
     * - Requires JWT authentication
     *
     * @param id user ID
     * @param request updated user details
     * @return success message
     */
    @PutMapping("/users/update/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable int id,
            @Valid @RequestBody UpdateUserRequest request) {

        userService.updateUser(id, request);
        return ResponseEntity.ok("User updated successfully");
    }
}
