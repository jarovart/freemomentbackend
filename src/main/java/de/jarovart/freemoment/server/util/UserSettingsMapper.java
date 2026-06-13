package de.jarovart.freemoment.server.util;

import de.jarovart.freemoment.server.model.dtos.response.SettingsResponse;
import de.jarovart.freemoment.server.model.entities.UserSetting;

public class UserSettingsMapper {
    public static SettingsResponse toSettingsResponse(UserSetting userSetting) {
        if (userSetting == null) {
            return null;
        }
        return new SettingsResponse(
                userSetting.getId(),
                userSetting.getLanguage().name(),
                userSetting.getAppDesign().name(),
                userSetting.getUpdatedAt()
        );
    }
}
