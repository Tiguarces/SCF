package pl.scf.api.model.exception;

public final class JwtException extends RuntimeException{
    public JwtException(final String message) {
        super(message);
    }
}
