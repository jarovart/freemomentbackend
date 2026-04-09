package de.jarovart.freemoment.server.services.controllerservices;

import de.jarovart.freemoment.server.model.dtos.requests.LoginRequest;
import de.jarovart.freemoment.server.model.dtos.requests.RegisterRequest;
import de.jarovart.freemoment.server.model.dtos.requests.ResetPasswordRequest;
import de.jarovart.freemoment.server.model.dtos.requests.SendEmailRequest;
import de.jarovart.freemoment.server.model.dtos.requests.VerifyTokenRequest;
import de.jarovart.freemoment.server.model.dtos.response.LoginResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.PasswordResetToken;
import de.jarovart.freemoment.server.model.entities.PendingUser;
import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.enums.UserRole;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.model.security.JarovartUserDetails;
import de.jarovart.freemoment.server.repository.PasswordResetTokenRepository;
import de.jarovart.freemoment.server.repository.PendingUserRepository;
import de.jarovart.freemoment.server.repository.UserRepository;
import de.jarovart.freemoment.server.services.JwtService;
import de.jarovart.freemoment.server.services.MailService;
import de.jarovart.freemoment.server.util.Util_General;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PendingUserRepository pendingUserRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public LoginResponse getLoginToken(LoginRequest loginRequest) {
        Optional<AppUser> optionalAppUser = userRepository.findByUsername(loginRequest.username());
        if (optionalAppUser.isEmpty() || !passwordEncoder.matches(loginRequest.password(),
                                                                  optionalAppUser.get().getPassword())) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "Invalid credentials",
                                               ErrorCode.INVALID_CREDENTIALS);
        }
        AppUser appUser = optionalAppUser.get();
        String token = jwtService.generateToken(appUser.getUsername(),
                                                Map.of("userId", appUser.getId(),
                                                       "roles", appUser.getRoles()
                                                                       .stream()
                                                                       .map(UserRole::getRoleName)
                                                                       .collect(Collectors.joining(","))));
        return new LoginResponse(token, appUser.getUsername(), appUser.getId());
    }

    @Transactional
    public void registerPendingUser(RegisterRequest registerRequest) {
        String email = registerRequest.email();
        String username = registerRequest.username();

        if (userRepository.findByUsername(username).isPresent()) {
            throw new ServiceResponseException(HttpStatus.CONFLICT, "Username exists", ErrorCode.USER_USERNAME_EXISTS);
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ServiceResponseException(HttpStatus.CONFLICT, "Email exists", ErrorCode.USER_EMAIL_EXISTS);
        }

        if (registerRequest.password() == null || registerRequest.password().isBlank()) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "Password is required",
                                               ErrorCode.USER_PASSWORD_REQUIRED);
        }

        if (registerRequest.password().length() < 8) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long",
                                               ErrorCode.USER_PASSWORD_TOO_SHORT);
        }

        if (email == null || email.isBlank() || !email.contains("@") || !email.contains(".") || email.contains(" ")) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "Invalid email", ErrorCode.USER_EMAIL_INVALID);
        }

        PendingUser pu = pendingUserRepository.findByEmail(email)
                                              .or(() -> pendingUserRepository.findByUsername(username))
                                              .orElseGet(PendingUser::new);
        if (pu.getLastMailSentAt() != null) {
            Util_General.verifyLastMailSentAtSeconds(pu.getLastMailSentAt());
        }

        String token = UUID.randomUUID().toString();
        pu.setUsername(username);
        pu.setFirstName(registerRequest.firstname());
        pu.setLastName(registerRequest.lastname());
        pu.setEmail(email);
        pu.setPasswordHash(passwordEncoder.encode(registerRequest.password()));
        pu.setVerifyToken(token);
        pu.setExpiresAt(LocalDateTime.now().plusHours(24));
        pu.setLastMailSentAt(LocalDateTime.now());
        pendingUserRepository.save(pu);
        mailService.sendVerificationMail(email, token);
    }

    @Transactional
    public void transferPendingUserToAppUser(VerifyTokenRequest verifyTokenRequest) {
        String token = verifyTokenRequest.token();
        if (token == null || token.isBlank()) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "Missing token", ErrorCode.TOKEN_MISSING);
        }

        PendingUser pu = pendingUserRepository
                .findByVerifyToken(token)
                .orElseThrow(() -> new ServiceResponseException(HttpStatus.BAD_REQUEST, "Invalid token",
                                                                ErrorCode.TOKEN_INVALID));

        if (pu.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ServiceResponseException(HttpStatus.GONE, "Token expired", ErrorCode.TOKEN_EXPIRED);
        }

        AppUser user = new AppUser();
        user.setUsername(pu.getUsername());
        user.setFirstName(pu.getFirstName());
        user.setLastName(pu.getLastName());
        user.setEmail(pu.getEmail());
        user.setPassword(pu.getPasswordHash());
        user.setRoles(Set.of(UserRole.ROLE_USER));
        user.setCreatedAt(LocalDateTime.now());
        user.setIsBanned(false);
        userRepository.save(user);
        pendingUserRepository.delete(pu);
    }

    @Transactional
    public void updateTokenOfPendingUserAndResendEmail(SendEmailRequest sendEmailRequest) {
        String email = sendEmailRequest.email();
        if (email == null || email.isBlank()) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "Email is required",
                                               ErrorCode.USER_EMAIL_REQUIRED);
        }

        Optional<PendingUser> optionalPendingUser = pendingUserRepository.findByEmail(email);
        if (optionalPendingUser.isEmpty()) {
            //log.warn("POST /api/auth/resend-verification - no pending user for email {}", email);
            return; // To prevent email enumeration
        }
        PendingUser pu = optionalPendingUser.get();
        Util_General.verifyLastMailSentAtSeconds(pu.getLastMailSentAt());

        String token = UUID.randomUUID().toString();
        pu.setVerifyToken(token);
        pu.setExpiresAt(LocalDateTime.now().plusHours(24));
        pu.setLastMailSentAt(LocalDateTime.now());
        pendingUserRepository.save(pu);

        String receiverEmail = pu.getEmail();
        mailService.sendVerificationMail(receiverEmail, token);
    }

    @Transactional
    public void requestResetPassword(SendEmailRequest sendEmailRequest) {
        String email = sendEmailRequest.email();
        if (email == null || email.isBlank() || !email.contains("@") || !email.contains(".")
                || email.trim().contains(" ")) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "Valid Email is required",
                                               ErrorCode.USER_EMAIL_INVALID);
        }

        // Immer 200, auch wenn email nicht existiert (anti enumeration)
        Optional<AppUser> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return;
        }

        // Rate Limit (60s)
        Optional<PasswordResetToken> existingOpt = passwordResetTokenRepository.findByEmail(email);

        existingOpt.ifPresent(token ->
                                      Util_General.verifyLastMailSentAtSeconds(token.getLastMailSentAt()));

        PasswordResetToken tokenEntity =
                existingOpt.orElse(new PasswordResetToken());
        tokenEntity.setEmail(email);
        tokenEntity.setToken(UUID.randomUUID().toString());
        tokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        tokenEntity.setLastMailSentAt(LocalDateTime.now());
        tokenEntity.setUsedAt(null);
        passwordResetTokenRepository.save(tokenEntity);
        mailService.sendPasswordResetMail(email, tokenEntity.getToken());
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository
                .findByToken(resetPasswordRequest.token())
                .orElseThrow(() -> new ServiceResponseException(HttpStatus.BAD_REQUEST, "Link is not valid",
                                                                ErrorCode.LINK_NOT_VALID));

        if (passwordResetToken.getUsedAt() != null) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "Link has already been used",
                                               ErrorCode.LINK_USED);
        }
        if (passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new ServiceResponseException(HttpStatus.GONE, "Link has expired", ErrorCode.LINK_EXPIRED);
        }

        String newPassword = resetPasswordRequest.newPassword();
        if (newPassword == null || newPassword.isBlank() || newPassword.length() < 8) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long",
                                               ErrorCode.USER_PASSWORD_TOO_SHORT);
        }

        AppUser user = userRepository
                .findByEmail(passwordResetToken.getEmail())
                .orElseThrow(() -> new ServiceResponseException(HttpStatus.NOT_FOUND, "User not found",
                                                                ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetToken.setUsedAt(LocalDateTime.now());
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepository.findByUsername(username)
                                     .orElseThrow(
                                             () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "User not found",
                                                                                ErrorCode.USER_NOT_FOUND));
        return new JarovartUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles()
                    .stream()
                    .map(UserRole::getRoleName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet())
        );
    }
}
