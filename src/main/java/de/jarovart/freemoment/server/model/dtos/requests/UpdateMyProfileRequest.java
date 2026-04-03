package de.jarovart.freemoment.server.model.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class UpdateMyProfileRequest {

    @NotBlank
    private String firstName;
    private String lastName;
    private String aboutMe;
    private MultipartFile profileImage;
}
