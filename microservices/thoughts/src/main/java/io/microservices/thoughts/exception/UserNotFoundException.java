package io.microservices.thoughts.exception;

/**
 * Custom exception thrown when a requested Thought is not found in the system.
 * This exception should be used to indicate that an operation could not complete
 * because the required Thought resource was not available.
 */
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ThoughtNotFoundException with {@code null} as its detail message.
     */
    public UserNotFoundException() {
        super();
    }

    /**
     * Constructs a new ThoughtNotFoundException with the specified detail message.
     *
     * @param message The detail message which provides more information about the reason for the exception.
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ThoughtNotFoundException with the specified detail message and cause.
     *
     * @param message The detail message providing more information about the reason for the exception.
     * @param cause   The cause of the exception, which can be used to chain exceptions.
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ThoughtNotFoundException with the specified cause.
     *
     * @param cause The cause of the exception, which can be used to chain exceptions.
     */
    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
