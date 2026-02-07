package com.hamid.usermanagement.repository;

import com.hamid.usermanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository
 *
 * =========================================================
 * WHY THIS INTERFACE IS REQUIRED
 * =========================================================
 *
 * Repository layer is responsible for ALL database operations.
 *
 * It acts as a bridge between:
 * ----------------------------
 * - Java application
 * - Database
 *
 * This interface allows us to perform CRUD operations
 * WITHOUT writing SQL queries manually.
 *
 * Spring Data JPA automatically generates implementations
 * at runtime based on method names.
 *
 * IMPORTANT:
 * ----------
 * - No business logic here
 * - No SQL here
 * - Only data access methods
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by email.
     *
     * HOW THIS WORKS:
     * ---------------
     * Spring Data JPA reads the method name:
     *   findByEmail
     *
     * And automatically generates this query:
     * ---------------------------------------
     * SELECT * FROM users WHERE email = ?
     *
     * WHY Optional<User>:
     * -------------------
     * - Avoids NullPointerException
     * - Forces caller to handle "user not found" case
     *
     * WHERE THIS IS USED:
     * -------------------
     * - Login (authenticate user)
     * - Registration (check duplicate email)
     *
     * @param email user's email
     * @return Optional containing User if found
     */
    Optional<User> findByEmail(String email);

}

