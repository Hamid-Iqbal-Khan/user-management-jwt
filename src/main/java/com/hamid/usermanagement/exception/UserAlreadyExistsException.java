package com.hamid.usermanagement.exception;

/**
 * UserAlreadyExistsException
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * This custom exception is thrown when a user tries to
 * register with an email that already exists in the system.
 *
 * WHY WE NEED A CUSTOM EXCEPTION:
 * -------------------------------
 * - Makes business errors explicit and readable
 * - Avoids using generic RuntimeException everywhere
 * - Helps GlobalExceptionHandler return correct HTTP status
 *
 * BUSINESS SCENARIO:
 * ------------------
 * - Registration request with duplicate email
 *
 * FLOW:
 * -----
 * Service Layer → throws UserAlreadyExistsException
 * GlobalExceptionHandler → catches it
 * Client → receives 400 Bad Request with message
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructor to pass custom error message.
     *
     * @param message explanation of the error
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
