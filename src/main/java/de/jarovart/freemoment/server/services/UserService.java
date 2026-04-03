package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.dtos.requests.UpdateMyProfileRequest;
import de.jarovart.freemoment.server.model.dtos.response.MyUserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.UserRepository;
import de.jarovart.freemoment.server.util.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {


    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        Pageable pageable = PageRequest.of(0, 20);
        return UserMapper.toUserResponse(userRepository.findAll(pageable).getContent());
    }

    public List<UserResponse> searchByQuery(String query) {
        if (query == null || query.trim().length() < 3) {
            return getAllUsers();
        }
        return UserMapper.toUserResponse(userRepository.searchUsers(query, PageRequest.of(0, 20)).getContent());
    }

    public Optional<UserFullResponse> findById(long id) {
        Optional<AppUser> appUser = userRepository.findById(id);
        return appUser.map(u -> {
            long countLikedLocations = userRepository.countLikedLocations(u.getId());
            long countJoinedLocations = userRepository.countJoinedLocations(u.getId());
            return UserMapper.toUserFullResponse(u, countLikedLocations,
                                                 countJoinedLocations);
        });
    }

    public MyUserFullResponse getMyProfile(Long userId) {
        AppUser appUser = userRepository.findByIdFull(userId)
                                        .orElseThrow(() -> new ServiceResponseException(HttpStatus.NOT_FOUND,
                                                                                        "USER_NOT_FOUND"));
        return evaluateMyUserFullResponse(appUser);
    }

    @Transactional
    public MyUserFullResponse updateMyProfile(Long userId, UpdateMyProfileRequest request) {
        AppUser user = userRepository.findByIdFull(userId)
                                     .orElseThrow(() -> new ServiceResponseException(HttpStatus.NOT_FOUND,
                                                                                     "USER_NOT_FOUND"));
        user.setFirstName(safeTrim(request.getFirstName()));
        user.setLastName(safeTrim(request.getLastName()));
        user.setAboutMe(safeTrim(request.getAboutMe()));
        AppUser savedUser = userRepository.save(user);
        return evaluateMyUserFullResponse(savedUser);
    }

    public MyUserFullResponse evaluateMyUserFullResponse(AppUser user) {
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
