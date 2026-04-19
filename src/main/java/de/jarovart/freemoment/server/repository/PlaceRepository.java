package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.model.entities.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByCreatorUser_Id(Long userId);

    Optional<Place> findByName(String name);

    @Query("""
                select p
                from Place p
                where lower(p.name) like lower(concat('%', :q, '%'))
                order by
                    case
                        when lower(p.name) = lower(:q) then 0
                        when lower(p.name) like lower(concat(:q, '%')) then 1
                        when lower(p.name) like lower(concat('%', :q, '%')) then 2
                        else 3
                    end,
                    length(p.name) asc,
                    p.name asc
            """)
    List<Place> findPlaceSuggestions(@Param("q") String query, Pageable pageable);
}
