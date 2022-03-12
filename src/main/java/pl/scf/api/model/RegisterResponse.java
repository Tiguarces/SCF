package pl.scf.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class RegisterResponse {
    private Boolean created;
    private String serverResponse;
    private Date date;
}
