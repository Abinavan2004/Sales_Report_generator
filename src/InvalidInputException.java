/**
 * Custom exception representing invalid command-line arguments,
 * unreadable files, or malformed input configurations.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
