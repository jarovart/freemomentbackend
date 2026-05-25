package de.jarovart.freemoment.server.services.controllerservices;

import de.jarovart.freemoment.server.model.dtos.requests.ImageRequest;
import de.jarovart.freemoment.server.model.dtos.requests.LocationCreateRequest;
import de.jarovart.freemoment.server.model.dtos.requests.UpdateMyLocationRequest;
import de.jarovart.freemoment.server.model.dtos.requests.UpdateThumbnailRequest;
import de.jarovart.freemoment.server.model.dtos.response.LocationFullResponse;
import de.jarovart.freemoment.server.model.dtos.response.LocationResponse;
import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Image;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.enums.ErrorCode;
import de.jarovart.freemoment.server.model.exception.ServiceResponseException;
import de.jarovart.freemoment.server.repository.LocationRepository;
import de.jarovart.freemoment.server.services.LocationJoiningService;
import de.jarovart.freemoment.server.services.LocationLikerService;
import de.jarovart.freemoment.server.services.LocationMappingService;
import de.jarovart.freemoment.server.util.LocationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional(readOnly = true)
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationMappingService locationMappingService;
    @Autowired
    private LocationLikerService locationLikerService;
    @Autowired
    private LocationJoiningService locationJoiningService;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;


    @Transactional
    public LocationResponse createLocation(LocationCreateRequest locationCreateRequest, Long userId) {
        AppUser user = userService.getUser(userId);
        Location location = LocationMapper.fromCreateRequest(locationCreateRequest, user);

        Location savedLocation = locationRepository.save(location);
        return locationMappingService.mapToLocationResponse(savedLocation, userId);
    }

    @Transactional
    public LocationResponse updateThumbnailLocation(Long locationId, UpdateThumbnailRequest updateThumbnailRequest,
                                                    Long userId) {
        Location location = getLocationFull(locationId);

        if (!location.getCreatedUser().getId().equals(userId)) {
            throw new ServiceResponseException(HttpStatus.FORBIDDEN, "NOT_LOCATION_OWNER",
                                               ErrorCode.LOCATION_FORBIDDEN);
        }

        Image image = imageService.getImage(updateThumbnailRequest.getImageId());

        if (image.getLocation() == null || !image.getLocation().getId().equals(locationId)) {
            throw new ServiceResponseException(HttpStatus.BAD_REQUEST, "IMAGE_NOT_BELONG_TO_LOCATION",
                                               ErrorCode.IMAGE_NOT_FOUND);
        }
        location.setThumbnailImage(image);
        Location savedLocation = locationRepository.save(location);
        return locationMappingService.mapToLocationResponse(savedLocation, userId);
    }

    @Transactional
    public LocationFullResponse updateMyLocation1(Long locationId, UpdateMyLocationRequest locationRequest,
                                                  Long userId) {
        Location location = getLocationFull(locationRequest.getId());

        if (!location.getCreatedUser().getId().equals(userId)) {
            throw new ServiceResponseException(HttpStatus.FORBIDDEN, "NOT_LOCATION_OWNER",
                                               ErrorCode.LOCATION_FORBIDDEN);
        }
        var mappedLocation = LocationMapper.fromUpdateRequest(location, locationRequest);
        Location savedLocation = locationRepository.save(mappedLocation);
        return locationMappingService.mapToLocationFullResponse(savedLocation, userId);
    }

    @Transactional
    public LocationFullResponse updateMyLocation(Long locationId, UpdateMyLocationRequest request,
                                                 List<MultipartFile> files, List<String> clientKeys, Long userId) {
        Location location = getLocationFull(locationId);

        if (!location.getCreatedUser().getId().equals(userId)) {
            throw new ServiceResponseException(HttpStatus.FORBIDDEN, "NOT_LOCATION_OWNER",
                                               ErrorCode.LOCATION_FORBIDDEN);
        }

        LocationMapper.fromUpdateRequest(location, request);
        Map<String, Image> newImagesByClientKey = imageService.storeLocationImagesMappedByClientKey(files, clientKeys,
                                                                                                    userId, location);
        List<Image> orderedImages = rebuildImageOrder(location, request.getImageRequests(), newImagesByClientKey);
        location.getImages().clear();

        IntStream.range(0, orderedImages.size()).forEach(i -> {
            Image image = orderedImages.get(i);
            image.setLocation(location);
            image.setSortIndex(i);
            location.getImages().add(image);
        });

        location.setThumbnailImage(orderedImages.isEmpty() ? null : orderedImages.getFirst());
        Location savedLocation = locationRepository.save(location);
        return locationMappingService.mapToLocationFullResponse(savedLocation, userId);
    }

    public LocationFullResponse getLocationById(Long id, Long userId) {
        Location location = locationRepository.findByIdFull(id).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "LOCATION_NOT_FOUND",
                                                   ErrorCode.LOCATION_NOT_FOUND));
        return locationMappingService.mapToLocationFullResponse(location, userId);
    }

    public boolean hasUserLiked(Long locationId, Long userId) {
        return locationLikerService.hasUserLiked(locationId, userId);
    }

    public boolean hasUserJoined(Long locationId, Long userId) {
        return locationJoiningService.hasUserJoined(locationId, userId);
    }

    @Transactional
    public void likeLocation(Long locationId, Long userId) {
        Location location = getLocationReference(locationId);
        AppUser user = userService.getUserReference(userId);

        locationLikerService.likeLocation(location, user);
    }

    @Transactional
    public void unlikeLocation(Long locationId, Long userId) {
        locationLikerService.unlikeLocation(locationId, userId);
    }

    @Transactional
    public void joinLocation(Long locationId, Long userId) {
        Location location = getLocationReference(locationId);
        AppUser user = userService.getUserReference(userId);

        locationJoiningService.joinLocation(location, user);
    }

    @Transactional
    public void unjoinLocation(Long locationId, Long userId) {
        locationJoiningService.unjoinLocation(locationId, userId);
    }

    public Location getLocation(Long locationId) {
        return locationRepository.findById(locationId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "LOCATION_NOT_FOUND",
                                                   ErrorCode.LOCATION_NOT_FOUND));
    }

    public Location getLocationFull(Long locationId) {
        return locationRepository.findByIdFull(locationId).orElseThrow(
                () -> new ServiceResponseException(HttpStatus.NOT_FOUND, "LOCATION_NOT_FOUND",
                                                   ErrorCode.LOCATION_NOT_FOUND));
    }

    public Location getLocationReference(Long locationId) {
        return locationRepository.getReferenceById(locationId);
    }

    public List<LocationResponse> search(String query, Long userId) {
        if (query == null || query.isBlank() || query.length() < 3) {
            return List.of();
        }
        Pageable limit = PageRequest.of(0, 10);
        return locationRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query, limit)
                                 .getContent()
                                 .stream()
                                 .map(loc -> locationMappingService.mapToLocationResponse(loc, userId))
                                 .toList();
    }

    public Slice<LocationResponse> getCreatedLocationsByUserIdPaged(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return locationRepository
                .findByCreatedUser_IdOrderByCreationDateTimeDesc(userId, pageable)
                .map(loc -> locationMappingService.mapToLocationResponse(loc, userId));
    }

    /************************************************************************************************
     * Old data
     */


    public List<LocationResponse> getAllLocations(int limit, Long userId) {
        Pageable pageable = PageRequest.of(0, Math.min(limit, 500));
        return locationRepository.findAll(pageable).getContent().stream()
                                 .map(loc -> locationMappingService.mapToLocationResponse(loc, userId)).toList();
    }

    public List<LocationResponse> getLocationsWithinBounds(double minLat, double maxLat, double minLng, double maxLng,
                                                           Long userId) {
        if (isInvalidBounds(minLat, maxLat, minLng, maxLng)) {
            return List.of();
        }
        Pageable limit = PageRequest.of(0, 200); // 🔥 Max Marker
        return locationRepository.findByLatitudeBetweenAndLongitudeBetween(minLat, maxLat, minLng, maxLng, limit)
                                 .stream()
                                 .map(loc -> locationMappingService.mapToLocationResponse(loc, userId))
                                 .toList();
    }

    public List<LocationResponse> getLocationsWithinBoundsAndRange(
            double minLat, double maxLat, double minLng, double maxLng, LocalDateTime rangeStart,
            LocalDateTime rangeEnd, Long userId) {
        if (rangeStart == null || rangeEnd == null || !(rangeStart.isBefore(rangeEnd) || rangeStart.isEqual(rangeEnd))
                || isInvalidBounds(minLat, maxLat, minLng, maxLng)) {
            return List.of();
        }
        Pageable limit = PageRequest.of(0, 200); // z.B. max Marker

        List<Location> locations = locationRepository.
                findWithinBoundsAndOverlappingRange(minLat, maxLat, minLng, maxLng, rangeStart, rangeEnd, limit);
        return locations.stream().map(loc -> locationMappingService.mapToLocationResponse(loc, userId)).toList();

    }

    public Slice<LocationResponse> getSliceLocationsByFilterSettings(
            int page,
            int paramSize,
            String query,
            double lat,
            double lng,
            double radiusKm,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Long userId
    ) {
        validateFilterInputs(lat, lng, radiusKm, startDateTime, endDateTime, page, paramSize);

        final int size = Math.min(Math.max(paramSize, 1), 50);
        final String normalizedQuery = normalizeQuery(query);

        double latDelta = radiusKm / 111.0;
        double lngDelta = radiusKm / (111.0 * Math.max(0.01, Math.cos(Math.toRadians(lat))));

        double minLat = lat - latDelta;
        double maxLat = lat + latDelta;
        double minLng = lng - lngDelta;
        double maxLng = lng + lngDelta;

        if (isInvalidBounds(minLat, maxLat, minLng, maxLng)) {
            throw new IllegalArgumentException("Invalid geographic bounds");
        }

        int neededMatchesBeforePage = page * size;
        int neededTotalMatches = neededMatchesBeforePage + size + 1; // +1 für hasNext

        List<Location> accepted = new ArrayList<>(neededTotalMatches);

        int dbPage = 0;
        final int dbChunkSize = Math.max(size * 3, 30);
        boolean moreRawData = true;

        while (accepted.size() < neededTotalMatches && moreRawData) {
            Pageable dbPageable = PageRequest.of(
                    dbPage,
                    dbChunkSize,
                    Sort.by(
                            Sort.Order.desc("creationDateTime"),
                            Sort.Order.desc("id")
                    )
            );

            List<Location> chunk = locationRepository.searchH2Chunk(
                    startDateTime,
                    endDateTime,
                    minLat,
                    maxLat,
                    minLng,
                    maxLng,
                    normalizedQuery,
                    dbPageable
            );

            if (chunk.isEmpty()) {
                moreRawData = false;
                break;
            }

            for (Location location : chunk) {
                if (haversineKm(lat, lng, location.getLatitude(), location.getLongitude()) <= radiusKm) {
                    accepted.add(location);
                    if (accepted.size() >= neededTotalMatches) {
                        break;
                    }
                }
            }

            if (chunk.size() < dbChunkSize) {
                moreRawData = false;
            }

            dbPage++;
        }

        int fromIndex = Math.min(neededMatchesBeforePage, accepted.size());
        int toExclusive = Math.min(fromIndex + size, accepted.size());

        List<LocationResponse> content = accepted.subList(fromIndex, toExclusive)
                                                 .stream()
                                                 .map(loc -> locationMappingService.mapToLocationResponse(loc, userId))
                                                 .toList();

        boolean hasNext = accepted.size() > (page + 1) * size;

        Pageable resultPageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("creationDateTime"),
                        Sort.Order.desc("id")
                )
        );

        return new SliceImpl<>(content, resultPageable, hasNext);
    }

    private String normalizeQuery(String query) {
        if (query == null) {
            return null;
        }
        String trimmed = query.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private void validateFilterInputs(
            double lat,
            double lng,
            double radiusKm,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            int page,
            int size
    ) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (lng < -180 || lng > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        if (radiusKm <= 0) {
            throw new IllegalArgumentException("radiusKm must be > 0");
        }
        if (startDateTime == null || endDateTime == null) {
            throw new IllegalArgumentException("rangeStart and rangeEnd are required");
        }
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("rangeEnd must not be before rangeStart");
        }
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }
    }

    private boolean isInvalidBounds(double minLat, double maxLat, double minLng, double maxLng) {
        return minLat < -90 || maxLat > 90 || minLng < -180 || maxLng > 180 || minLat >= maxLat || minLng >= maxLng;
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double r = 6371.0088;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);

        return 2 * r * Math.asin(Math.sqrt(a));
    }

    private List<Image> rebuildImageOrder(Location location, List<ImageRequest> imageRequests,
                                          Map<String, Image> newImagesByClientKey) {
        Map<Long, Image> existingImagesById = location.getImages()
                                                      .stream()
                                                      .collect(Collectors.toMap(Image::getId, Function.identity()));

        return imageRequests.stream()
                            .sorted(Comparator.comparing(ImageRequest::getSortIndex))
                            .map(item -> {
                                if (item.getIsNew()) {
                                    Image image = newImagesByClientKey.get(item.getClientKey());
                                    if (image == null) {
                                        throw new ServiceResponseException(HttpStatus.BAD_REQUEST,
                                                                           "NEW_IMAGE_NOT_FOUND",
                                                                           ErrorCode.IMAGE_NOT_FOUND);
                                    }
                                    return image;
                                }

                                Image image = existingImagesById.get(item.getId());
                                if (image == null) {
                                    throw new ServiceResponseException(HttpStatus.BAD_REQUEST,
                                                                       "IMAGE_NOT_BELONG_TO_LOCATION",
                                                                       ErrorCode.IMAGE_NOT_FOUND);
                                }
                                return image;
                            }).toList();
    }
}
