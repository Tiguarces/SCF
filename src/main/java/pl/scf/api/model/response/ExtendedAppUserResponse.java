package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.api.model.dto.ExtendedAppUserDTO;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ExtendedAppUserResponse extends Response{
    private ExtendedAppUserDTO appUser;
}
