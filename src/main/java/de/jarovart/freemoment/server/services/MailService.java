package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.exception.SendingEmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationMail(String receiverEmail, String token) throws SendingEmailException {
        String link = "https://meetmaap.app/verifyemail?token=" + token;

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
            mailSender.send(message);
        } catch (MailException e) {
            throw new SendingEmailException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void sendPasswordResetMail(String receiverEmail, String token) {
        String link = "https://meetmaap.app/reset-password?token=" + token;

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
            mailSender.send(message);
        } catch (MailException e) {
            throw new SendingEmailException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}

