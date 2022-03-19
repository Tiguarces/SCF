package pl.scf.model.property;

import lombok.Data;

@Data
public class JWTProperty {
    private String secret_password;
    private Long expired_time;
    private String issuer;
}
