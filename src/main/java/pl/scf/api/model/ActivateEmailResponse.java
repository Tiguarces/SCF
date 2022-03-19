package pl.scf.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public final class ActivateEmailResponse {
    private Boolean activated;
    private String response;
    private Date date;
    private String nickname;
}