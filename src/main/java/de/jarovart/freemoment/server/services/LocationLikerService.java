package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.entities.LocationLike;
import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.LocationLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional(readOnly = true)
public class LocationLikerService {
    @Autowired
    private LocationLikeRepository locationLikeRepository;


    public boolean hasUserLiked(Long locationId, Long userId) {
        return locationLikeRepository.existsByLocation_IdAndUser_Id(locationId, userId);
    }

    public long getCountByUserId(Long userId) {
        return locationLikeRepository.countByUser_Id(userId);
    }

    public long getCountByLocationId(Long locationId) {
        return locationLikeRepository.countByLocation_Id(locationId);
    }

    public Slice<Location> getLikedLocationsByUserIdPaged(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return locationLikeRepository.findLocationsByUserIdOrderByLikedAtDesc(userId, pageable);
    }

    public Slice<AppUser> getLikedUsersByLocationIdPaged(Long locationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return locationLikeRepository.findUsersByLocationIdOrderByLikedAtDesc(locationId, pageable);
    }

    @Transactional
    public void likeLocation(Location location, AppUser user) {
        LocationLike locationLike = new LocationLike();
        locationLike.setUser(user);
        locationLike.setLocation(location);
        locationLike.setLikedAt(LocalDateTime.now());
        locationLikeRepository.save(locationLike);
    }

    @Transactional
    public void unlikeLocation(Long locationId, Long userId) {
        LocationLike locationLike = getLocationLikeFromDbById(locationId, userId);
        locationLikeRepository.delete(locationLike);
    }

    public LocationLike getLocationLikeFromDbById(Long locationId, Long userId) {
        return locationLikeRepository.findByLocation_IdAndUser_Id(locationId, userId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "LOCATION_LIKE_NOT_FOUND",
                                                   ErrorCode.LOCATION_LIKE_NOT_FOUND));
    }
}
