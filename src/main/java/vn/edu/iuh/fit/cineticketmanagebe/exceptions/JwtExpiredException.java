package vn.edu.iuh.fit.cineticketmanagebe.exceptions;

public class JwtExpiredException extends RuntimeException {

    public JwtExpiredException(String message) {
        super(message);
    }

    public JwtExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
