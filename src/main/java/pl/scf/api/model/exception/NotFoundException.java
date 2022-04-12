package pl.scf.api.model.exception;

public final class NotFoundException extends RuntimeException{
    public NotFoundException(final String message) {
        super(message);
    }

    public NotFoundException() {
        super();
    }
}
