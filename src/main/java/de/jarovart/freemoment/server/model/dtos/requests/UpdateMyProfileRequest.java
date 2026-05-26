package de.jarovart.freemoment.server.model.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateMyProfileRequest {

    @NotBlank
    private String firstName;
    private String lastName;
    private String aboutMe;
    private boolean removeProfileImage;
}
