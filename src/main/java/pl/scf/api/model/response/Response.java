package pl.scf.api.model.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
public abstract class Response {
    protected String message;
    protected Boolean success;
    protected Instant date;
}
