package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.VerificationTokenDTO;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class VerificationTokenResponse extends Response{
    private VerificationTokenDTO verificationToken;
}
