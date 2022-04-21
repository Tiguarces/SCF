package pl.scf.api.model.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.scf.api.model.exception.IdentificationException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;

public interface ResponseUtil {
    static <T> ResponseEntity<T> buildResponseEntity(HttpStatus status, T response) {
        return ResponseEntity
                .status(status)
                .body(response);
    }

    static String formatDate(final Instant date) {
        final LocalDateTime time = LocalDateTime.ofInstant(date, ZoneOffset.UTC);
        final int year = time.getYear();
        final String month = translateMonth(time.getMonth());
        final int day = time.getDayOfMonth();
        
        final String spaceSeparator = "  ";
        return day + spaceSeparator +
               month + spaceSeparator +
               year  + spaceSeparator;
    }

    private static String translateMonth(final Month month) {
        return switch (month) {
            case JANUARY -> "Styczeń";
            case FEBRUARY -> "Luty";
            case MARCH -> "Marzec";
            case APRIL -> "Kwiecień";
            case MAY -> "Maj";
            case JUNE -> "Czerwiec";
            case JULY -> "Lipiec";
            case AUGUST -> "Sierpień";
            case SEPTEMBER -> "Wrzesień";
            case OCTOBER -> "Październik";
            case NOVEMBER -> "Listopad";
            case DECEMBER -> "Grudzień";
        };
    }

    static void throwExceptionWhenIdZero(final Long id) throws IdentificationException {
        if(id == 0)
            throw new IdentificationException(id);
    }
}
