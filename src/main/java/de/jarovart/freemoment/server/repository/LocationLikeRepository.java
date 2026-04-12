package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.entities.LocationLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationLikeRepository extends JpaRepository<LocationLike, Long> {

    boolean existsByLocation_IdAndUser_Id(Long locationId, Long userId);

    long countByLocation_Id(Long locationId);

    long countByUser_Id(Long userId);

    Optional<LocationLike> findByLocation_IdAndUser_Id(Long locationId, Long userId);

    Slice<LocationLike> findByUser_IdOrderByLikedAtDesc(Long userId, Pageable pageable);

    Slice<LocationLike> findByLocation_IdOrderByLikedAtDesc(Long userId, Pageable pageable);

    @Query("""
                select ll.location
                from LocationLike ll
                where ll.user.id = :userId
                order by ll.likedAt desc
            """)
    Slice<Location> findLocationsByUserIdOrderByLikedAtDesc(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
                select ll.user
                from LocationLike ll
                where ll.location.id = :locationId
                order by ll.likedAt desc
            """)
    Slice<AppUser> findUsersByLocationIdOrderByLikedAtDesc(
            @Param("locationId") Long locationId,
            Pageable pageable
    );
}
