package pl.scf.api.model.request;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
