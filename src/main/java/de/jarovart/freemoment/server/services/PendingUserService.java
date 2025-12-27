package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.entities.PendingUser;
import de.jarovart.freemoment.server.repository.PendingUserRepository;
import de.jarovart.freemoment.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PendingUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PendingUserRepository pendingUserRepository;
    @Autowired
    private MailService mailService;


    @Transactional
    public void updateTokenOfPendingUser(PendingUser pu) {
        String token = UUID.randomUUID().toString();
        String receiverEmail = pu.getEmail();
        pu.setVerifyToken(token);
        pu.setExpiresAt(LocalDateTime.now().plusHours(24));
        pu.setLastMailSentAt(LocalDateTime.now());
        pendingUserRepository.save(pu);
        mailService.sendVerificationMail(receiverEmail, token);
    }

    public PendingUser findByVerifyToken(String token) {
        return pendingUserRepository.findByVerifyToken(token).orElse(null);
    }
}
