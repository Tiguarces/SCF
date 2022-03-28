package pl.scf.api.model.request;

import lombok.Data;

@Data
public class UserRoleUpdateRequest {
    private Long roleId;
    private String name;
}
