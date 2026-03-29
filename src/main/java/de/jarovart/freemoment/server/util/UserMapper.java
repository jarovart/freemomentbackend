package de.jarovart.freemoment.server.util;

import de.jarovart.freemoment.server.model.dtos.response.MyUserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.UserResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;

import java.util.List;

public class UserMapper {
    public static List<UserResponse> toUserResponse(List<AppUser> users) {
        return users.stream().map(UserMapper::toUserResponse).toList();
    }

    public static UserResponse toUserResponse(AppUser user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                ImageMapper.toImageResponse(user.getProfileImage()));
    }

    public static UserFullResponse toUserFullResponse(AppUser user, long countLikedLocations,
                                                      long countJoinedLocations) {
        return new UserFullResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                ImageMapper.toImageResponse(user.getProfileImage()),
                user.getAboutMe(),
                countLikedLocations,
                countJoinedLocations);
    }

    public static MyUserFullResponse toMyUserFullResponse(AppUser user, long countLikedLocations,
                                                          long countJoinedLocations, long createdLocations) {
        return new MyUserFullResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                ImageMapper.toImageResponse(user.getProfileImage()),
                user.getAboutMe(),
                countLikedLocations,
                countJoinedLocations,
                user.getEmail(),
                user.getCreatedAt(),
                createdLocations);
    }
}
