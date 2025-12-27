package de.jarovart.freemoment.server.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class PendingUser {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;

    private String verifyToken;
    private LocalDateTime expiresAt;
    @Column(name = "last_mail_sent_at")
    private LocalDateTime lastMailSentAt;

    public PendingUser() {
    }
}
