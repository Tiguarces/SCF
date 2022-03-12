package pl.scf.model.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailNotification {
    private String subject;
    private String recipient;
    private String from;
    private String content;
    private String token;
    private String username;
}
