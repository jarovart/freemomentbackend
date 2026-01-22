package de.jarovart.freemoment.server.controller;

import de.jarovart.freemoment.server.model.dtos.response.ImageResponse;
import de.jarovart.freemoment.server.services.controllerservices.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final Logger log =
            LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageService imageService;

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> serve(@PathVariable String filename) {

        Path file = Paths.get("uploads").resolve(filename);
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                             .contentType(MediaType.IMAGE_JPEG)
                             .body(resource);
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ImageResponse>> upload(
            @RequestParam("files") List<MultipartFile> files, @AuthenticationPrincipal UserDetails principal) {
        log.info("POST /upload request: {} from {}",
                 files.stream().map(MultipartFile::getName).collect(Collectors.joining(", ")), principal.getUsername());
        
        List<ImageResponse> imageResponses = imageService.store(files, principal.getUsername());
        return ResponseEntity.ok(imageResponses);
    }

    @DeleteMapping("/images/{imageId}")
    public void deleteImage(@PathVariable Long imageId) {
        imageService.delete(imageId);
    }

}
