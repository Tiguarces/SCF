package pl.scf.api.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import pl.scf.model.AppUser;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AppUserResponse extends Response {
    private AppUser user;
}
