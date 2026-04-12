package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.entities.LocationJoin;
import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.LocationJoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional(readOnly = true)
public class LocationJoiningService {
    @Autowired
    private LocationJoinRepository locationJoinRepository;

    public boolean hasUserJoined(Long locationId, Long userId) {
        return locationJoinRepository.existsByLocation_IdAndUser_Id(locationId, userId);
    }

    public long getCountByUserId(Long userId) {
        return locationJoinRepository.countByUser_Id(userId);
    }

    public long getCountByLocationId(Long locationId) {
        return locationJoinRepository.countByLocation_Id(locationId);
    }

    public Slice<Location> getJoinedLocationsByUserIdPaged(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "joinedAt"));

        return locationJoinRepository.findLocationsByUserIdOrderByJoinedAtDesc(userId, pageable);
    }

    public Slice<AppUser> getJoinedUsersByLocationIddPaged(Long locationId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "joinedAt"));

        return locationJoinRepository.findUsersByLocationIdOrderByJoinedAtDesc(locationId, pageable);
    }

    @Transactional
    public void joinLocation(Location location, AppUser user) {
        LocationJoin locationJoin = new LocationJoin();
        locationJoin.setUser(user);
        locationJoin.setLocation(location);
        locationJoin.setJoinedAt(LocalDateTime.now());
        locationJoinRepository.save(locationJoin);
    }

    @Transactional
    public void unjoinLocation(Long locationId, Long userId) {
        LocationJoin locationJoin = getLocationJoinFromDbById(locationId, userId);
        locationJoinRepository.delete(locationJoin);
    }

    public LocationJoin getLocationJoinFromDbById(Long locationId, Long userId) {
        return locationJoinRepository.findByLocation_IdAndUser_Id(locationId, userId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "LOCATION_LIKE_NOT_FOUND",
                                                   ErrorCode.LOCATION_JOIN_NOT_FOUND));
    }
}
