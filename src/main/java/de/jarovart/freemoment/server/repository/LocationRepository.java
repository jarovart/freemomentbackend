/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.jarovart.freemoment.server.repository;
import de.jarovart.freemoment.server.data.Location;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Artem
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    
    public List<Location> findByLatitudeBetweenAndLongitudeBetween(
        double minLat, double maxLat,
        double minLng, double maxLng
    );
    
    @Query("SELECT l FROM Location l WHERE l.latitude BETWEEN :minLat AND :maxLat AND l.longitude BETWEEN :minLng AND :maxLng")
    public List<Location> findWithinBounds(
      @Param("minLat") double minLat,
      @Param("maxLat") double maxLat,
      @Param("minLng") double minLng,
      @Param("maxLng") double maxLng);
}
