package fitness.club.exeptions;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
