package de.jarovart.freemoment.server.services.controllerservices;

import de.jarovart.freemoment.server.model.dtos.response.ImageResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Image;
import de.jarovart.freemoment.server.repository.ImageRepository;
import de.jarovart.freemoment.server.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ImageService {

    private final Path root = Paths.get("uploads");
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<ImageResponse> store(List<MultipartFile> files, String username) {
        AppUser user = userRepository.findByUsername(username)
                                     .orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<ImageResponse> imageResponses = new ArrayList<>();

        try {
            Files.createDirectories(root);
            for (MultipartFile file : files) {
                String filename = UUID.randomUUID() + ".jpg";
                Path target = root.resolve(filename);

                Files.copy(file.getInputStream(), target);

                Image image = new Image();
                image.setFilename(filename);
                image.setAppUser(user);
                image.setCreationDateTime(LocalDateTime.now());
                image.setContentType(file.getContentType());
                image.setSize(file.getSize());
                Image savedImage = imageRepository.save(image);
                imageResponses.add(new ImageResponse(savedImage.getId(), savedImage.getUrl()));
            }
            return imageResponses;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image");
        }
    }

    public void delete(Long imageId) {
    }
}
