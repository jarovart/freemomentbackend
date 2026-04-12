package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.entities.LocationJoin;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationJoinRepository extends JpaRepository<LocationJoin, Long> {

    boolean existsByLocation_IdAndUser_Id(Long locationId, Long userId);

    long countByLocation_Id(Long locationId);

    long countByUser_Id(Long userId);

    Optional<LocationJoin> findByLocation_IdAndUser_Id(Long locationId, Long userId);

    Slice<LocationJoin> findByUser_IdOrderByJoinedAtDesc(Long userId, Pageable pageable);

    Slice<LocationJoin> findByLocation_IdOrderByJoinedAtDesc(Long locationId, Pageable pageable);

    @Query("""
                select lj.location
                from LocationJoin lj
                where lj.user.id = :userId
                order by lj.joinedAt desc
            """)
    Slice<Location> findLocationsByUserIdOrderByJoinedAtDesc(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
                select lj.user
                from LocationJoin lj
                where lj.location.id = :locationId
                order by lj.joinedAt desc
            """)
    Slice<AppUser> findUsersByLocationIdOrderByJoinedAtDesc(
            @Param("locationId") Long locationId,
            Pageable pageable
    );
}
