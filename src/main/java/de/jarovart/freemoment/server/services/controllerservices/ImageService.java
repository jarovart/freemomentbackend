package de.jarovart.freemoment.server.services.controllerservices;

import de.jarovart.freemoment.server.model.dtos.requests.ImageRequest;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ImageService {

    private final Path root = Paths.get("uploads");
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private LocationImageService locationImageService;

    public Image getImage(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "IMAGE_NOT_FOUND",
                                                   ErrorCode.IMAGE_NOT_FOUND));
    }

    @Transactional
    public List<ImageResponse> storeImages(List<MultipartFile> files, AppUser user) {
        List<Image> images = storeUploadedImages(files, user, null);
        return createImageResponses(images);
    }

    @Transactional
    public List<ImageResponse> storeImages(List<MultipartFile> files, AppUser user, Long locationId) {
        Location location = locationImageService.getLocationWithCreatedUserAndImages(locationId);

        if (!location.getCreatedUser().getId().equals(user.getId())) {
            throw new ServiceResponseException(HttpStatus.FORBIDDEN, "NOT_LOCATION_OWNER", ErrorCode.IMAGE_NOT_FOUND);
        }

        List<Image> images = storeUploadedImages(files, user, location);
        location.getImages().addAll(images);
        return createImageResponses(images);
    }

    @Transactional
    public ImageResponse uploadMyProfileImage(MultipartFile file, AppUser user, boolean removeOldImage) {
        Image oldImage = user.getProfileImage();
        Image newImage = storeUploadedImage(file, user);
        user.setProfileImage(newImage);
        //userService.save(user);
        if (oldImage != null && removeOldImage) {
            imageRepository.delete(oldImage);
            deleteImage(oldImage.getFilename());
        }
        return createImageResponse(newImage);
    }

    @Transactional
    public void deleteMyProfileImage(AppUser user) {
        Image image = user.getProfileImage();
        if (image == null) {
            return;
        }

        user.setProfileImage(null);
        user.getUploadedImages().remove(image);
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
    public void updateProfileImage(AppUser user, MultipartFile file, boolean shouldRemoveImage) {
        if (shouldRemoveImage || file != null) {
            deleteMyProfileImage(user);
        }

        if (file != null) {
            Image image = storeUploadedImage(file, user);
            user.setProfileImage(image);
        }
    }

    @Transactional
    public void updateLocationImages(List<ImageRequest> imageRequests, List<MultipartFile> newUploadFiles,
                                     Location location) {
        List<Image> locationImages = imageRepository.findByLocation_IdOrderByIdAsc(location.getId()).stream().toList();

        List<Long> imageRequestIds = imageRequests.stream().map(ImageRequest::getId).filter(Objects::nonNull).toList();
        List<String> filesToDelete = locationImages.stream().filter(img -> !imageRequestIds.contains(img.getId()))
                                                   .map(Image::getFilename).toList();
        List<Image> addedImaged = storeUploadedImages(newUploadFiles, location.getCreatedUser(), null);

        List<Image> imagesToSet = new ArrayList<>();
        for (ImageRequest imageRequest : imageRequests) {
            if (imageRequest.getIsNew() && !addedImaged.isEmpty()) {
                imagesToSet.add(addedImaged.removeFirst());
            } else {
                imagesToSet.add(imageRepository.getReferenceById(imageRequest.getId()));
            }
        }

        location.getImages().clear();
        for (Image image : imagesToSet) {
            image.setLocation(location);
            location.getImages().add(image);
        }

        if (!imagesToSet.isEmpty()) {
            location.setThumbnailImage(imagesToSet.getFirst());
        }
        filesToDelete.forEach(this::deleteImage);
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
