/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.data.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Artem
 */
public interface LocationRepository extends JpaRepository<Location, Long> {

    public List<Location> findByLatitudeBetweenAndLongitudeBetween(double minLat, double maxLat, double minLng,
                                                                   double maxLng, Pageable pageable);

    @Query("SELECT l FROM Location l WHERE l.latitude BETWEEN :minLat AND :maxLat AND l.longitude BETWEEN :minLng AND :maxLng")
    public List<Location> findWithinBounds(@Param("minLat") double minLat, @Param("maxLat") double maxLat,
                                           @Param("minLng") double minLng, @Param("maxLng") double maxLng);

    Page<Location> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description,
                                                                                    Pageable pageable);

    @Query("""
            select l from Location l
            where l.latitude between :minLat and :maxLat
              and l.longitude between :minLng and :maxLng
              and l.startDateTime <= :rangeEnd
              and l.endDateTime >= :rangeStart
            """)
    List<Location> findWithinBoundsAndOverlappingRange(@Param("minLat") double minLat, @Param("maxLat") double maxLat,
                                                       @Param("minLng") double minLng, @Param("maxLng") double maxLng,
                                                       @Param("rangeStart") LocalDateTime rangeStart,
                                                       @Param("rangeEnd") LocalDateTime rangeEnd, Pageable pageable
    );

    @Query("""
                select l from Location l
                left join fetch l.createdUser
                left join fetch l.joinedUsers
                left join fetch l.likedByUsers
                where l.id = :id
            """)
    Optional<Location> findByIdWithUsers(@Param("id") Long id);
}
