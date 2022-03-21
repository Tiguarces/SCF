package pl.scf.api.model;

import lombok.Data;

@Data
public class AppUserDetailsUpdateRequest {
    private String email;
    private Long AppUserDetailsUserId;
}
