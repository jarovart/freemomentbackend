/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.configurations;

import de.jarovart.freemoment.server.data.Location;
import de.jarovart.freemoment.server.repository.LocationRepository;
import java.time.LocalDateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Artem
 */
@Configuration
public class DbSeeding {
     
    @Bean
    public CommandLineRunner init(LocationRepository locationRepository) {
        return args -> {
            locationRepository.save(new Location("Bärlin", "berlinbär", 
                    LocalDateTime.now(),  52.52, 13.4));
            locationRepository.save(new Location("Mauer", "berlinmauer", 
                    LocalDateTime.now(),  52.51, 13.42));
            locationRepository.save(new Location("Stand", "berlinstand", 
                    LocalDateTime.now(),  52.53, 13.39));
        };
    }
}
