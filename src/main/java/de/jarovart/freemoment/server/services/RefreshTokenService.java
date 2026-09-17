package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.RefreshToken;
import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.RefreshTokenRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
public class RefreshTokenService {

    private static final long REFRESH_TOKEN_VALIDITY_DAYS = 30;

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String issueRefreshToken(AppUser user) {
        String rawToken = generateRawToken();
        String hash = hash(rawToken);

        RefreshToken entity = new RefreshToken();
        entity.setTokenHash(hash);
        entity.setUser(user);
        entity.setExpiresAt(Instant.now().plus(REFRESH_TOKEN_VALIDITY_DAYS, ChronoUnit.DAYS));
        refreshTokenRepository.save(entity);

        return rawToken; // der Rohtoken geht raus, nur der Hash bleibt in der DB
    }

    public AppUser validateAndRotate(String rawToken) {
        String hash = hash(rawToken);
        RefreshToken entity = refreshTokenRepository.findByTokenHashAndRevokedFalse(hash)
                                                    .orElseThrow(() -> new ServiceResponseException(
                                                            HttpStatus.UNAUTHORIZED, "Invalid refresh token",
                                                            ErrorCode.INVALID_REFRESH_TOKEN));

        if (entity.getExpiresAt().isBefore(Instant.now())) {
            throw new ServiceResponseException(
                    HttpStatus.UNAUTHORIZED, "Refresh token expired", ErrorCode.INVALID_REFRESH_TOKEN);
        }

        entity.setRevoked(true); // alten Token sofort invalidieren (Rotation)
        refreshTokenRepository.save(entity);

        return entity.getUser();
    }

    public void revokeAllForUser(AppUser user) {
        refreshTokenRepository.deleteByUser(user);
    }

    private String generateRawToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hash(String rawToken) {
        return DigestUtils.sha256Hex(rawToken);
    }
}