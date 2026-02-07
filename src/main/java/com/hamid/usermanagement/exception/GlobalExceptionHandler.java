package com.hamid.usermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * In a real application, errors should NOT:
 * - Return stack traces to clients
 * - Return confusing default error pages
 * - Be handled repeatedly in every controller
 *
 * This class CENTRALIZES exception handling
 * for the entire application.
 *
 * BENEFITS:
 * ---------
 * - Clean and consistent error responses
 * - No try-catch blocks in controllers
 * - Proper HTTP status codes
 * - Easy maintenance
 *
 * @RestControllerAdvice means:
 * - This class applies to ALL controllers
 * - Responses are returned as JSON / plain text
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // =====================================================
    // HANDLE DUPLICATE USER REGISTRATION
    // =====================================================

    /**
     * Handles UserAlreadyExistsException.
     *
     * WHEN THIS OCCURS:
     * -----------------
     * - User tries to register with an email
     *   that already exists in the database
     *
     * WHY THIS HANDLER EXISTS:
     * ------------------------
     * - Registration errors are client mistakes
     * - Client should be informed clearly
     *
     * HTTP STATUS:
     * ------------
     * 400 BAD REQUEST
     *
     * @param ex custom exception
     * @return error message to client
     */
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleDuplicate(
            UserAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    // =====================================================
    // HANDLE USER NOT FOUND
    // =====================================================

    /**
     * Handles UserNotExistException.
     *
     * WHEN THIS OCCURS:
     * -----------------
     * - User not found during delete/update
     * - Login attempt with non-existing user
     *
     * WHY THIS HANDLER EXISTS:
     * ------------------------
     * - Resource requested by client does not exist
     *
     * HTTP STATUS:
     * ------------
     * 404 NOT FOUND
     *
     * @param ex custom exception
     * @return error message to client
     */
    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<String> handleUserNotExists(
            UserNotExistException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}

