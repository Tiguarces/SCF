package pl.scf.api.model.request;

import lombok.Data;

@Data
public class VerificationTokenSaveRequest {
    private Long userId;
    private String token;
    private Integer activated;
}
