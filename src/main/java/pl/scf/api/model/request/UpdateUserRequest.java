package pl.scf.api.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateUserRequest {
    private String username;
    private String password;
}
