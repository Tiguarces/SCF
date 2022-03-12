package pl.scf.model.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {
    @Async
    public final void sendEmail(final MailNotification mailNotification, final JavaMailSender mailSender) {
        final MimeMessagePreparator messagePreparator = (mimeMessage -> {
            final MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            messageHelper.setTo(mailNotification.getRecipient());
            messageHelper.setFrom(mailNotification.getFrom());

            messageHelper.setSubject(mailNotification.getSubject());
            messageHelper.setText(String.format(mailNotification.getContent(), mailNotification.getUsername(), mailNotification.getToken()));
        });

        try {
            mailSender.send(messagePreparator);
            log.info("Email was sent successfully to {}", mailNotification.getUsername());
        } catch (MailException exception) {
            log.error("Errors while sending email to {} | {}", mailNotification.getUsername(), exception.getMessage());
        }
    }
}
