package de.jarovart.freemoment.server.model.dtos.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String profileUrl;

    private UserResponse() {
    }

    public UserResponse(Long id, String username, String firstName, String lastName, String profileUrl) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileUrl = profileUrl;
    }
}
