package de.jarovart.freemoment.server.model.dtos.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class MyUserFullResponse extends UserFullResponse {

    private String email;
    private LocalDateTime createdAt;
    private long createdLocations;

    public MyUserFullResponse(Long id, String username, String firstName, String lastName, String profileUrl,
                              String aboutMe, long likedLocationCount, long joinedLocationCount,
                              String email, LocalDateTime createdAt, long createdLocations) {
        super(id, username, firstName, lastName, profileUrl, aboutMe, likedLocationCount, joinedLocationCount);
        this.email = email;
        this.createdAt = createdAt;
        this.createdLocations = createdLocations;
    }
}
