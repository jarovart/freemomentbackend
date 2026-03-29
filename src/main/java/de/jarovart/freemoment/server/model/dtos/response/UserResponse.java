package de.jarovart.freemoment.server.model.dtos.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private ImageResponse profileImage;

    public UserResponse() {
    }

    public UserResponse(Long id, String username, String firstName, String lastName, ImageResponse profileImage) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImage = profileImage;
    }
}
