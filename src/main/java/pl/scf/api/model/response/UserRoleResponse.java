package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.UserRoleDTO;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class UserRoleResponse extends Response {
    private UserRoleDTO userRole;
}
