package pl.scf.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
    private String username;
    private String password;
}
