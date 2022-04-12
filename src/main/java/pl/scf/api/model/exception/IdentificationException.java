package pl.scf.api.model.exception;

public final class IdentificationException extends RuntimeException{
    public IdentificationException() {
        super();
    }

    private Long id;
    public IdentificationException(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
