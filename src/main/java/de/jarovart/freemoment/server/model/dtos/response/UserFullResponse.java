package de.jarovart.freemoment.server.model.dtos.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFullResponse extends UserResponse {
    private String aboutMe;
    private long likedLocationCount;
    private long joinedLocationCount;

    private UserFullResponse() {
        super();
    }

    public UserFullResponse(Long id, String username, String firstName, String lastName, ImageResponse profileImage,
                            String aboutMe, long likedLocationCount, long joinedLocationCount) {
        super(id, username, firstName, lastName, profileImage);
        this.aboutMe = aboutMe;
        this.likedLocationCount = likedLocationCount;
        this.joinedLocationCount = joinedLocationCount;

    }
}
