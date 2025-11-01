/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package de.jarovart.freemoment.server.repository;
import de.jarovart.freemoment.server.data.Location;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Artem
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    
}
