package pl.scf.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class UniversalResponse {
    private Date date;
    private String response;
    private Boolean success;
}
