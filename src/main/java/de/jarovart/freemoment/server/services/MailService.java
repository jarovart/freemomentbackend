package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.exception.SendingEmailException;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private static final int VERIFICATION_VALID_HOURS = 24;
    private static final int PASSWORD_RESET_VALID_MINUTES = 15;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${app.frontend.url}")
    private String frontendUrl;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MessageSource messageSource;

    public void sendVerificationMail(String username, String receiverEmail, String token,
                                     String languageCode) throws SendingEmailException {
        Locale locale = toLocale(languageCode);
        String link = frontendUrl + "/verifyemail?token=" + token;

        send(receiverEmail,
             messageSource.getMessage("verify.subject", null, locale),
             messageSource.getMessage(
                     "verify.body",
                     new Object[]{username, link, VERIFICATION_VALID_HOURS},
                     locale
             ),
             "verification"
        );
    }

    public void sendPasswordResetMail(String username, String receiverEmail, String token, String languageCode) {
        Locale locale = toLocale(languageCode);
        String link = frontendUrl + "/resetpassword?token=" + token;

        send(receiverEmail,
             messageSource.getMessage("reset.subject", null, locale),
             messageSource.getMessage(
                     "reset.body",
                     new Object[]{username, link, PASSWORD_RESET_VALID_MINUTES},
                     locale
             ),
             "password reset"
        );
    }

    private void send(String receiverEmail, String subject, String body, String kind) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(receiverEmail);
        message.setSubject(subject);
        message.setText(body);

        try {
            log.info("Sending {} mail to {}", kind, receiverEmail);
            mailSender.send(message);
            log.info("{} mail sent to {}", kind, receiverEmail);
        } catch (MailException e) {
            log.error("Failed to send {} mail to {}", kind, receiverEmail, e);
            throw new ServiceResponseException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    ErrorCode.USER_EMAIL_INVALID
            );
        }
    }

    private Locale toLocale(String code) {
        if (code == null) {
            return Locale.ENGLISH;
        }
        return switch (code.toUpperCase()) {
            case "DE" -> Locale.GERMAN;
            default -> Locale.ENGLISH;
        };
    }
}

