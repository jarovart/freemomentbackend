/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.model.entities.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    List<Location> findByLatitudeBetweenAndLongitudeBetween(double minLat, double maxLat, double minLng,
                                                            double maxLng, Pageable pageable);

    Page<Location> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description,
                                                                                    Pageable pageable);

    @Query("""
            select l from Location l
            left join fetch l.thumbnailImage
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
                left join fetch l.images
                left join fetch l.thumbnailImage
                where l.id = :id
            """)
    Optional<Location> findByIdWithCreatedUserAndImages(@Param("id") Long id);

    @Query("""
                select l from Location l
                left join fetch l.thumbnailImage
                left join fetch l.images
                left join fetch l.createdUser
                left join fetch l.likes
                left join fetch l.joins
                where l.id = :id
            """)
    Optional<Location> findByIdFull(@Param("id") Long locationId);


    List<Location> findByCreatedUser_Id(Long userId);

    Slice<Location> findByCreatedUser_IdOrderByCreationDateTimeDesc(
            Long userId,
            Pageable pageable
    );

    /***************************************************************************************************
     *
     * Outdated methods.
     *
     ***************************************************************************************************/

    @Query("""
                select l from Location l
                left join fetch l.createdUser
                left join fetch l.joins
                left join fetch l.likes
                left join fetch l.images
                left join fetch l.thumbnailImage
                where l.id = :id
            """)
    Optional<Location> findByIdWithUsers(@Param("id") Long id);

    Page<Location> findByStartDateTimeGreaterThanEqualAndEndDateTimeLessThanEqualAndLatitudeBetweenAndLongitudeBetween(
            LocalDateTime start, LocalDateTime end,
            double minLat, double maxLat,
            double minLng, double maxLng,
            Pageable pageable
    );

    @Query("""
            SELECT l FROM Location l
            left join fetch l.thumbnailImage
            WHERE l.latitude
            BETWEEN :minLat AND :maxLat AND l.longitude
            BETWEEN :minLng AND :maxLng""")
    List<Location> findWithinBounds(@Param("minLat") double minLat, @Param("maxLat") double maxLat,
                                    @Param("minLng") double minLng, @Param("maxLng") double maxLng);

    @Query("""
            select l from Location l
            left join fetch l.thumbnailImage
            where l.startDateTime <= :rangeEnd
              and l.endDateTime >= :rangeStart
              and l.latitude between :minLat and :maxLat
              and l.longitude between :minLng and :maxLng
              and (
                    :q is null or :q = '' or
                    lower(l.title) like lower(concat('%', :q, '%')) or
                    lower(l.description) like lower(concat('%', :q, '%'))
                  )
            order by l.creationDateTime desc, l.id desc
            """)
    List<Location> searchH2Chunk(
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            @Param("q") String q,
            Pageable pageable
    );

    @Query(value = """
            select * from locations l
            left join fetch l.thumbnailImage
            where l.start_date_time >= :start and l.end_date_time <= :end
            and (:q is null or l.title ilike concat('%', :q, '%') or l.description ilike concat('%', :q, '%'))
            and ST_DWithin(
              l.position::geography,
              ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
              :radiusMeters
            )
            order by l.creation_date_time desc, l.id desc
            """,
            countQuery = """
                    select count(*) from locations l
                    where l.start_date_time >= :start and l.end_date_time <= :end
                    and (:q is null or l.title ilike concat('%', :q, '%') or l.description ilike concat('%', :q, '%'))
                    and ST_DWithin(
                      l.position::geography,
                      ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
                      :radiusMeters
                    )
                    """, nativeQuery = true)
    Page<Location> searchPostgis(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLng") double minLng,
            @Param("maxLng") double maxLng,
            @Param("q") String q, Pageable pageable);
}
