package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class RefreshTokenResponse extends Response{
    private String accessToken;
    private String refreshToken;
    private String username;
}
