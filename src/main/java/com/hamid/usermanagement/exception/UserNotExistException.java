package com.hamid.usermanagement.exception;

/**
 * UserNotExistException
 *
 * =========================================================
 * WHY THIS CLASS IS REQUIRED
 * =========================================================
 *
 * This custom exception is thrown when an operation
 * expects a user to exist but the user is not found
 * in the database.
 *
 * WHY WE NEED A CUSTOM EXCEPTION:
 * -------------------------------
 * - Clearly represents a "user not found" business error
 * - Avoids using vague RuntimeException
 * - Allows proper HTTP status mapping (404 Not Found)
 *
 * BUSINESS SCENARIOS:
 * -------------------
 * - Delete user with invalid ID
 * - Update user that does not exist
 * - Fetch user that does not exist
 *
 * FLOW:
 * -----
 * Service Layer → throws UserNotExistException
 * GlobalExceptionHandler → catches it
 * Client → receives 404 Not Found with message
 */
public class UserNotExistException extends RuntimeException {

    /**
     * Constructor to pass custom error message.
     *
     * @param message explanation of the error
     */
    public UserNotExistException(String message) {
        super(message);
    }
}
