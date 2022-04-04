package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationTokenDTO {
    private String token;
    private Integer activated;
    private Long userId;
}
