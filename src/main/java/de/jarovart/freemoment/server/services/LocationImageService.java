package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.ImageRepository;
import de.jarovart.freemoment.server.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LocationImageService {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private LocationRepository locationRepository;

    public Location getLocationWithCreatedUserAndImages(Long locationId) {
        return locationRepository.findByIdWithCreatedUserAndImages(locationId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "LOCATION_NOT_FOUND",
                                                   ErrorCode.LOCATION_NOT_FOUND));
    }
}
