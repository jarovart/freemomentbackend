package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.exception.SendingEmailException;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${app.frontend.url}")
    private String frontendUrl;
    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationMail(String receiverEmail, String token) throws SendingEmailException {
        String link = frontendUrl + "/verifyemail?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(receiverEmail);
        message.setSubject("Meetmaap – E-Mail bestätigen");
        message.setText("""
                                Willkommen bei Meetmaap 👋
                                
                                Bitte bestätige deine E-Mail-Adresse:
                                %s
                                
                                Der Link ist 24 Stunden gültig.
                                """.formatted(link));

        try {
            log.info("Sending verification mail to {}", receiverEmail);
            mailSender.send(message);
            log.info("Verification mail sent to {}", receiverEmail);
        } catch (MailException e) {
            throw new ServiceResponseException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    ErrorCode.USER_EMAIL_INVALID
            );
        }
    }

    public void sendPasswordResetMail(String receiverEmail, String token) {
        String link = frontendUrl + "/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailUsername);
        message.setTo(receiverEmail);
        message.setSubject("Meetmaap – Passwort zurücksetzen");
        message.setText("""
                                Willkommen bei Meetmaap 👋
                                
                                Bitte klicke auf den folgenden Link, um dein Passwort zurückzusetzen:
                                %s
                                
                                Der Link ist 15 Minuten gültig.
                                """.formatted(link));

        try {
            log.info("Sending password reset mail to {}", receiverEmail);
            mailSender.send(message);
            log.info("password reset mail sent to {}", receiverEmail);
        } catch (MailException e) {
            throw new ServiceResponseException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    e.getMessage(),
                    ErrorCode.USER_EMAIL_INVALID
            );
        }
    }
}

