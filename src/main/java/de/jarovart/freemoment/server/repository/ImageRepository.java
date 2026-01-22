package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.model.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {


}
