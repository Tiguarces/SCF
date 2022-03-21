package pl.scf.api.model;

import lombok.Data;

@Data
public class UserRoleUpdateRequest {
    private Long roleId;
    private String name;
}
