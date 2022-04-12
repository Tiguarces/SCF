package pl.scf.api.model.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.scf.api.model.exception.IdentificationException;

public interface ResponseUtil {
    static <T> ResponseEntity<T> buildResponseEntity(HttpStatus status, T response) {
        return ResponseEntity
                .status(status)
                .body(response);
    }

    static void throwExceptionWhenIdZero(final Long id) throws IdentificationException {
        if(id == 0)
            throw new IdentificationException(id);
    }
}
