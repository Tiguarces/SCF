package pl.scf.model.property;

import lombok.Data;

@Data
public class AdministratorAccountProperty {
    private String username;
    private String password;
    private String nickname;
    private String email;
}
