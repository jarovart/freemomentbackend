package de.jarovart.freemoment.server.services.controllerservices;

import de.jarovart.freemoment.server.model.dtos.response.ImageResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Image;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.ImageRepository;
import de.jarovart.freemoment.server.services.LocationImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
public class ImageService {

    private final Path root = Paths.get("uploads");
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private LocationImageService locationImageService;

    public Image getImage(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "IMAGE_NOT_FOUND",
                                                   ErrorCode.IMAGE_NOT_FOUND));
    }

    @Transactional
    public List<ImageResponse> storeImages(List<MultipartFile> files, Long userId) {
        AppUser user = userService.getUserReference(userId);
        List<Image> images = storeUploadedImages(files, user, null);
        return createImageResponses(images);
    }

    @Transactional
    public List<ImageResponse> storeImages(List<MultipartFile> files, Long userId, Long locationId) {
        Location location = locationImageService.getLocationWithCreatedUserAndImages(locationId);

        if (!location.getCreatedUser().getId().equals(userId)) {
            throw new ServiceResponseException(HttpStatus.FORBIDDEN, "NOT_LOCATION_OWNER", ErrorCode.IMAGE_NOT_FOUND);
        }
        AppUser user = userService.getUser(userId);

        List<Image> images = storeUploadedImages(files, user, location);
        location.getImages().addAll(images);
        return createImageResponses(images);
    }

    @Transactional
    public ImageResponse uploadMyProfileImage(MultipartFile file, Long userId) {
        AppUser user = userService.getUserFull(userId);
        Image oldImage = user.getProfileImage();
        Image newImage = storeUploadedImage(file, user);
        user.setProfileImage(newImage);
        userService.save(user);
        if (oldImage != null) {
            imageRepository.delete(oldImage);
            deleteImage(oldImage.getFilename());
        }
        return createImageResponse(newImage);
    }

    @Transactional
    public void deleteMyProfileImage(Long userId) {
        AppUser user = userService.getUserFull(userId);
        Image image = user.getProfileImage();
        if (image == null) {
            return;
        }

        user.setProfileImage(null);
        imageRepository.delete(image);
        deleteImage(image.getFilename());
    }

    @Transactional
    public void delete(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.FORBIDDEN, "NOT_LOCATION_OWNER",
                                                   ErrorCode.IMAGE_NOT_FOUND));
        imageRepository.delete(image);
        deleteImage(image.getFilename());
    }

    @Transactional
    public Map<String, Image> storeLocationImagesMappedByClientKey(List<MultipartFile> files, List<String> clientKeys,
                                                                   Long userId, Location location) {
        if (files.size() != clientKeys.size()) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "IMAGE_CLIENT_KEY_MISMATCH",
                                               ErrorCode.IMAGE_NOT_FOUND);
        }
        AppUser user = userService.getUser(userId);
        Map<String, Image> result = new HashMap<>();

        IntStream.range(0, files.size()).forEach(i -> {
            Image image = storeUploadedImages(List.of(files.get(i)), user, location).getFirst();
            result.put(clientKeys.get(i), image);
        });
        return result;
    }

    private ImageResponse createImageResponse(Image image) {
        return new ImageResponse(image.getId(), image.getFilename());
    }

    private List<ImageResponse> createImageResponses(List<Image> images) {
        return images.stream().map(image -> new ImageResponse(image.getId(), image.getFilename())).toList();
    }

    private Image storeUploadedImage(MultipartFile file, AppUser user) {
        return storeUploadedImages(Collections.singletonList(file), user, null).getFirst();
    }

    private List<Image> storeUploadedImages(List<MultipartFile> files, AppUser user, Location location) {
        List<Image> savedImages = new ArrayList<>();

        try {
            Files.createDirectories(root);
            for (MultipartFile file : files) {
                String filename = UUID.randomUUID() + ".jpg";
                Path target = root.resolve(filename);

                Files.copy(file.getInputStream(), target);

                Image image = new Image();
                image.setFilename(filename);
                image.setUploadedByUser(user);
                image.setCreationDateTime(LocalDateTime.now());
                image.setContentType(file.getContentType());
                image.setSize(file.getSize());
                if (location != null) {
                    image.setLocation(location);
                }
                Image savedImage = imageRepository.save(image);
                savedImages.add(savedImage);
            }
            return savedImages;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store image");
        }
    }

    private void deleteImage(String filename) {
        try {
            Path file = root.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + filename);
        }
    }
}
