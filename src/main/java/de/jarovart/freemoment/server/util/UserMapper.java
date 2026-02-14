package de.jarovart.freemoment.server.util;

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
                user.getProfileUrl());
    }

    public static UserFullResponse toUserFullResponse(AppUser user, long countLikedLocations,
                                                      long countJoinedLocations) {
        return new UserFullResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfileUrl(),
                user.getAboutMe(),
                countLikedLocations,
                countJoinedLocations);
    }
}
