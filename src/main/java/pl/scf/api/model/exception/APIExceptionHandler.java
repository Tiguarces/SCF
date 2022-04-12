package pl.scf.api.model.exception;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.scf.api.model.response.UniversalResponse;

import java.time.Instant;

import static org.springframework.http.HttpStatus.*;
import static pl.scf.api.model.utils.ApiConstants.AUTHENTICATE_USER_FAIL;
import static pl.scf.api.model.utils.ApiConstants.ID_ERROR_MESSAGE;
import static pl.scf.api.model.utils.ResponseUtil.buildResponseEntity;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<UniversalResponse> handleNotFoundException(final NotFoundException exception) {
        log.warn(exception.getMessage());
        return buildResponseEntity(NOT_FOUND, UniversalResponse.builder()
                .success(false)
                .date(Instant.now())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(IdentificationException.class)
    public final ResponseEntity<UniversalResponse> handleNullableIdException(final IdentificationException exception) {
        log.warn(ID_ERROR_MESSAGE);
        return buildResponseEntity(BAD_REQUEST, UniversalResponse.builder()
                .success(false)
                .date(Instant.now())
                .message(exception.getMessage() == null ? ID_ERROR_MESSAGE : exception.getMessage())
                .build()
        );
    }

    @ExceptionHandler({ JWTVerificationException.class, JwtException.class, TokenExpiredException.class, JWTDecodeException.class})
    public final ResponseEntity<UniversalResponse> handleJwtTokenException(final RuntimeException exception) {
        log.warn(exception.getMessage());
        return buildResponseEntity(FORBIDDEN, UniversalResponse.builder()
                .success(false)
                .date(Instant.now())
                .message(exception.getMessage())
                .build());
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<UniversalResponse> handleAuthenticationException(final AuthenticationException exception) {
        log.warn(AUTHENTICATE_USER_FAIL);
        return buildResponseEntity(FORBIDDEN, UniversalResponse.builder()
                .success(false)
                .date(Instant.now())
                .message(exception.getMessage())
                .build());
    }
}
