package pl.scf.api.model.exception;

public class JwtException extends RuntimeException{
    public JwtException(final String message) {
        super(message);
    }
}
