package pl.scf.api.model.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
public abstract class Response {
    protected String message;
    protected Boolean success;
    protected Date date;
}
