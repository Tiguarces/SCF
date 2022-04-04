package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.AppUserDetailsDTO;
import pl.scf.model.AppUserDetails;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AppUserDetailsResponse extends Response{
    private AppUserDetailsDTO details;
}
