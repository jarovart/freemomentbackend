package de.jarovart.freemoment.server.model.dtos.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFullResponse extends UserResponse {
    private String aboutMe;
    private long likedLocationCount;
    private long joinedLocationCount;

    public UserFullResponse(Long id, String username, String firstName, String lastName, String profileUrl,
                            String aboutMe, long likedLocationCount, long joinedLocationCount) {
        super(id, username, firstName, lastName, profileUrl);
        this.aboutMe = aboutMe;
        this.likedLocationCount = likedLocationCount;
        this.joinedLocationCount = joinedLocationCount;

    }
}
