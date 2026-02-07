package com.hamid.usermanagement.service.impl;

import com.hamid.usermanagement.config.JwtUtil;
import com.hamid.usermanagement.dto.LoginRequest;
import com.hamid.usermanagement.dto.RegisterRequest;
import com.hamid.usermanagement.dto.UpdateUserRequest;
import com.hamid.usermanagement.entity.User;
import com.hamid.usermanagement.exception.UserAlreadyExistsException;
import com.hamid.usermanagement.exception.UserNotExistException;
import com.hamid.usermanagement.repository.UserRepository;
import com.hamid.usermanagement.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * This class contains the ACTUAL BUSINESS LOGIC
 * related to user management.
 *
 * It implements the UserService interface and defines
 * HOW each operation is performed.
 *
 * RESPONSIBILITIES:
 * -----------------
 * - Enforce business rules
 * - Interact with repository layer
 * - Handle security-related logic (password, JWT)
 * - Throw meaningful business exceptions
 *
 * Controller MUST NOT contain this logic.
 */
@Service                     // Marks this as a Spring service bean
@Transactional               // Ensures DB operations are atomic
@AllArgsConstructor          // Constructor injection for dependencies
public class UserServiceImpl implements UserService {

    // Repository used to interact with database
    private final UserRepository userRepository;

    // Used to hash and verify passwords securely
    private final PasswordEncoder passwordEncoder;

    // Used to generate and validate JWT tokens
    private final JwtUtil jwtUtil;

    // =====================================================
    // REGISTER USER
    // =====================================================

    /**
     * Registers a new user.
     *
     * BUSINESS RULES:
     * ---------------
     * 1. Email must be unique
     * 2. Password must be encrypted
     * 3. Default role must be assigned
     *
     * @param request registration data from client
     */
    @Override
    public void register(RegisterRequest request) {

        // Step 1: Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already registered");
        }

        // Step 2: Create User entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Step 3: Encrypt password before saving
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Step 4: Assign default role
        user.setRole("USER");

        // Step 5: Save user to database
        userRepository.save(user);
    }

    // =====================================================
    // LOGIN USER
    // =====================================================

    /**
     * Authenticates user and returns JWT token.
     *
     * BUSINESS RULES:
     * ---------------
     * 1. User must exist
     * 2. Password must match encrypted password
     * 3. JWT token generated on success
     *
     * @param loginRequest login credentials
     * @return JWT token
     */
    @Override
    public String login(LoginRequest loginRequest) {

        // Step 1: Fetch user by email
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid Credential"));

        // Step 2: Validate password
        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid Credential");
        }

        // Step 3: Generate JWT token
        return jwtUtil.generateToken(user.getEmail());
    }

    // =====================================================
    // DELETE USER
    // =====================================================

    /**
     * Deletes a user by ID.
     *
     * BUSINESS RULES:
     * ---------------
     * - User must exist
     *
     * @param id user ID
     */
    @Override
    public void deleteUser(int id) {

        // Step 1: Find user
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotExistException(
                                "User not found with id: " + id));

        // Step 2: Delete user
        userRepository.delete(user);
    }

    // =====================================================
    // UPDATE USER
    // =====================================================

    /**
     * Updates user details.
     *
     * BUSINESS RULES:
     * ---------------
     * - User must exist
     * - Updated password must be encrypted
     *
     * @param id user ID
     * @param userRequest updated user data
     */
    @Override
    public void updateUser(int id, UpdateUserRequest userRequest) {

        // Step 1: Fetch existing user
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotExistException(
                                "User not found with id: " + id));

        // Step 2: Update allowed fields
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());

        // Step 3: Encrypt new password
        user.setPassword(
                passwordEncoder.encode(userRequest.getPassword())
        );

        // Step 4: Save updated user
        userRepository.save(user);
    }
}
