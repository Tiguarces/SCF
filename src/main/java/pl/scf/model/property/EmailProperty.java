package pl.scf.model.property;

import lombok.Data;

@Data
public class EmailProperty {
    private String secret_password;
    private String email_subject;
    private String email_content;
    private String email_from;
}
