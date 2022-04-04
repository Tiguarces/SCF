package pl.scf.api.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserDTO {
    private String username;
    private String roleName;
    private Long detailsId;
    private Long verTokenId;
}
