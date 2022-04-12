package pl.scf.api.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExtendedUserRoleDTO {
    private Long id;
    private String name;
}
