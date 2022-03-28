package pl.scf.api.model.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static pl.scf.api.model.utils.ApiConstants.ID_ERROR_MESSAGE;
import static pl.scf.api.model.utils.ApiConstants.NOT_FOUND_MESSAGE;

public final class ResponseUtil {
    public static String messageByIdError(final Long id, final String toMessageWord) {
        return id == 0
                ? ID_ERROR_MESSAGE
                : String.format(NOT_FOUND_MESSAGE, toMessageWord, "id", id);
    }

    public static String messageBySthError(final String sthWord, final String sth, final String toMessageWord) {
        return String.format(NOT_FOUND_MESSAGE, toMessageWord, sthWord, sth);
    }

    public static <T> ResponseEntity<T> buildResponseEntity(HttpStatus status, T response) {
        return ResponseEntity
                .status(status)
                .body(response);
    }
}
