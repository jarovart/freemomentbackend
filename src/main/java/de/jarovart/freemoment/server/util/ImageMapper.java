package de.jarovart.freemoment.server.util;

import de.jarovart.freemoment.server.model.dtos.response.ImageResponse;
import de.jarovart.freemoment.server.model.entities.Image;

public class ImageMapper {

    public static ImageResponse toImageResponse(Image image) {
        if (image == null) {
            return null;
        }
        return new ImageResponse(image.getId(), image.getUrl());
    }
}
