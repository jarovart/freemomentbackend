package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.dtos.requests.RefreshRequest;
import de.jarovart.freemoment.server.model.dtos.response.RefreshResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthRefreshService {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthRefreshService(JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public RefreshResponse refresh(RefreshRequest request) {
        AppUser user = refreshTokenService.validateAndRotate(request.refreshToken());

        String newAccessToken = jwtService.generateToken(user.getUsername(),
                                                         Map.of("userId", user.getId(),
                                                                "roles", user.getRoles().stream()
                                                                             .map(UserRole::getRoleName)
                                                                             .collect(Collectors.joining(","))));
        String newRefreshToken = refreshTokenService.issueRefreshToken(user);

        return new RefreshResponse(newAccessToken, newRefreshToken);
    }
}
