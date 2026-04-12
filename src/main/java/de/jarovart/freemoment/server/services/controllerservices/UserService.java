package de.jarovart.freemoment.server.services.controllerservices;

import de.jarovart.freemoment.server.model.dtos.requests.UpdateMyProfileRequest;
import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.dtos.response.MyUserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.UserRepository;
import de.jarovart.freemoment.server.services.LocationJoiningService;
import de.jarovart.freemoment.server.services.LocationLikerService;
import de.jarovart.freemoment.server.services.LocationMappingService;
import de.jarovart.freemoment.server.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationLikerService locationLikerService;
    @Autowired
    private LocationJoiningService locationJoiningService;

    @Autowired
    private LocationMappingService locationMappingService;

    public AppUser getUserReference(Long userId) {
        return userRepository.getReferenceById(userId);
    }

    public AppUser getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND",
                                                   ErrorCode.USER_NOT_FOUND));
    }

    public AppUser getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND",
                                                   ErrorCode.USER_NOT_FOUND));
    }

    public AppUser getUserFull(Long userId) {
        return userRepository.findByIdFull(userId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND",
                                                   ErrorCode.USER_NOT_FOUND));
    }

    public Slice<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return userRepository.findAll(pageable).map(UserMapper::toUserResponse);
    }

    @Transactional
    public AppUser save(AppUser user) {
        return userRepository.save(user);
    }

    public Slice<LocationResponse> getLikedLocationsByUserId(Long userId, int page, int pageSize) {
        return locationLikerService.getLikedLocationsByUserIdPaged(userId, page, pageSize)
                                   .map(loc -> locationMappingService.mapToLocationResponse(loc, userId));

    }

    public Slice<LocationResponse> getJoinedLocationsByUserId(Long userId, int page, int pageSize) {
        return locationJoiningService.getJoinedLocationsByUserIdPaged(userId, page, pageSize)
                                     .map(loc -> locationMappingService.mapToLocationResponse(loc, userId));
    }

    /*************************************************************************************************
     * Old
     */


    public List<UserResponse> searchByQuery(String query) {
        if (query == null || query.trim().length() < 3) {
            return getAllUsers(0, 10).getContent();
        }
        return UserMapper.toUserResponse(userRepository.searchUsers(query, PageRequest.of(0, 20)).getContent());
    }

    public UserFullResponse findById(long id) {
        AppUser appUser = userRepository.findById(id).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ErrorCode.USER_NOT_FOUND));

        long countLikedLocations = userRepository.countLikedLocations(appUser.getId());
        long countJoinedLocations = userRepository.countJoinedLocations(appUser.getId());
        return UserMapper.toUserFullResponse(appUser, countLikedLocations, countJoinedLocations);
    }

    public UserFullResponse findByUsername(String username) {
        AppUser appUser = userRepository.findByUsername(username)
                                        .orElseThrow(() -> new ServiceResponseException(HttpStatus.NOT_FOUND,
                                                                                        "USER_NOT_FOUND",
                                                                                        ErrorCode.USER_NOT_FOUND));

        long countLikedLocations = userRepository.countLikedLocations(appUser.getId());
        long countJoinedLocations = userRepository.countJoinedLocations(appUser.getId());
        return UserMapper.toUserFullResponse(appUser, countLikedLocations, countJoinedLocations);
    }

    public MyUserFullResponse getMyProfile(Long userId) {
        AppUser appUser = userRepository.findByIdFull(userId)
                                        .orElseThrow(() -> new ServiceResponseException(HttpStatus.NOT_FOUND,
                                                                                        "USER_NOT_FOUND",
                                                                                        ErrorCode.USER_NOT_FOUND));
        return mapToMyUserFullResponse(appUser);
    }

    @Transactional
    public MyUserFullResponse updateMyProfile(Long userId, UpdateMyProfileRequest request) {
        AppUser user = userRepository.findByIdFull(userId)
                                     .orElseThrow(() -> new ServiceResponseException(HttpStatus.NOT_FOUND,
                                                                                     "USER_NOT_FOUND",
                                                                                     ErrorCode.USER_NOT_FOUND));
        user.setFirstName(safeTrim(request.getFirstName()));
        user.setLastName(safeTrim(request.getLastName()));
        user.setAboutMe(safeTrim(request.getAboutMe()));
        AppUser savedUser = userRepository.save(user);
        return mapToMyUserFullResponse(savedUser);
    }

    public MyUserFullResponse mapToMyUserFullResponse(AppUser user) {
        long countLikedLocations = userRepository.countLikedLocations(user.getId());
        long countJoinedLocations = userRepository.countJoinedLocations(user.getId());
        long countCreatedLocations = userRepository.countCreatedLocations(user.getId());

        return UserMapper.toMyUserFullResponse(user, countLikedLocations, countJoinedLocations,
                                               countCreatedLocations);
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }
}
