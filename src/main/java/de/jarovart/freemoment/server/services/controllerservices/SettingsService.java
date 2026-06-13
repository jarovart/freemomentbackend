package de.jarovart.freemoment.server.services.controllerservices;


import de.jarovart.freemoment.server.model.dtos.requests.SettingsRequest;
import de.jarovart.freemoment.server.model.dtos.response.SettingsResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.UserSetting;
import de.jarovart.freemoment.server.model.enums.AppDesignEnum;
import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.enums.LanguageEnum;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.UserSettingsRepository;
import de.jarovart.freemoment.server.util.UserSettingsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SettingsService {

    @Autowired
    private UserSettingsRepository userSettingsRepository;
    @Autowired
    private UserService userService;

    public SettingsResponse getMySettings(Long userId) {
        UserSetting userSetting = userSettingsRepository.findByUserId(userId)
                                                        .orElseThrow(() -> new ServiceResponseException(
                                                                HttpStatus.NOT_FOUND,
                                                                "SETTINGS_NOT_FOUND",
                                                                ErrorCode.SETTINGS_NOT_FOUND));
        return UserSettingsMapper.toSettingsResponse(userSetting);
    }

    @Transactional
    public SettingsResponse saveMySettings(SettingsRequest settingsRequest, Long userId) {
        UserSetting userSetting = userSettingsRepository.findByUserId(userId).orElse(null);
        if (userSetting == null) {
            AppUser user = userService.getUserReference(userId);
            userSetting = new UserSetting();
            userSetting.setUser(user);
        }
        userSetting.setLanguage(LanguageEnum.from(settingsRequest.getLocale()));
        userSetting.setAppDesign(AppDesignEnum.from(settingsRequest.getDesign()));
        userSetting.setUpdatedAt(settingsRequest.getUpdatedAt());

        UserSetting savedUserSetting = userSettingsRepository.save(userSetting);
        return UserSettingsMapper.toSettingsResponse(savedUserSetting);
    }
}
