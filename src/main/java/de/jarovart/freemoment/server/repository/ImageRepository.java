package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.model.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByLocation_IdOrderByIdAsc(Long locationId);
}
