package de.jarovart.freemoment.server.services.controllerservices;

import de.jarovart.freemoment.server.model.dtos.response.StatusFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.StatusResponse;
import de.jarovart.freemoment.server.model.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
@Transactional(readOnly = true)
public class InfoService {

    private final BuildProperties buildProperties;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private UserService userService;

    public InfoService(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    public StatusResponse getServerInfo() {
        return new StatusResponse(true);
    }

    public StatusResponse getFullServerInfo(Long id) {
        boolean isUser = userService.getUser(id).getRoles().stream()
                                    .noneMatch(r -> r.equals(UserRole.ROLE_ROOT) || r.equals(UserRole.ROLE_ADMIN));

        if (isUser) {
            return new StatusResponse(true);
        }

        boolean databaseOnline;
        try (Connection connection = dataSource.getConnection()) {
            databaseOnline = connection.isValid(2);
        } catch (Exception e) {
            databaseOnline = false;
        }
        return new StatusFullResponse(true, databaseOnline, buildProperties.getVersion());
    }
}
