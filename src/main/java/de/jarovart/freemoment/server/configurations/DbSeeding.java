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
            locationRepository.save(new Location("NordMauer", "berlinmauer", 
                    LocalDateTime.now(),  53.23994798679545, 8.795070648193361));
            locationRepository.save(new Location("OstBärlin", "berlinbär", 
                    LocalDateTime.now(),  53.22710439096767, 8.808116912841799));
            locationRepository.save(new Location("WestSide", "berlinstand", 
                    LocalDateTime.now(),  53.22957066025697, 8.766231536865236));
            locationRepository.save(new Location("SüdStand", "berlinstand", 
                    LocalDateTime.now(),  53.20191976452034, 8.760223388671877));
        };
    }
}
