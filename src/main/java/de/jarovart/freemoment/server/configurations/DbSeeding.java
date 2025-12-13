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
                    LocalDateTime.now(),  53.23994798679545, 8.795070648193361,
                    "thumbnailUrl", "imageUrl", "user1"));
            locationRepository.save(new Location("OstBärlin", "berlinbär", 
                    LocalDateTime.now(),  53.22710439096767, 8.808116912841799,
                    "thumbnailUrl", "imageUrl", "user2"));
            locationRepository.save(new Location("WestSide", "berlinstand", 
                    LocalDateTime.now(),  53.22957066025697, 8.766231536865236,
                    "thumbnailUrl", "imageUrl", "user3"));
            locationRepository.save(new Location("SüdStand", "berlinstand", 
                    LocalDateTime.now(),  53.20191976452034, 8.760223388671877,
                    "thumbnailUrl", "imageUrl", "user4"));
            locationRepository.save(new Location("aldi", "standmauer",
                    LocalDateTime.now(),  53.195762929604136, 8.627364440380877,
                    "thumbnailUrl5", "imageUrl5", "user5"));
            locationRepository.save(new Location("schweinewede", "standbärchen",
                    LocalDateTime.now(),  53.22393166002433, 8.598868651806658,
                    "thumbnailUrl6", "imageUrl6", "user6"));
            locationRepository.save(new Location("eastcoast", "berlinsaft",
                    LocalDateTime.now(),  53.22867195051402, 8.795378619754933,
                    "thumbnailUrl7", "imageUrl7", "user7"));
            locationRepository.save(new Location("labbers", "",
                    LocalDateTime.now(),  53.22256785805446, 8.795135853907508,
                    "thumbnailUrl8", "imageUrl8", "user8"));
            locationRepository.save(new Location("rittersbude", "fleischerhaken",
                    LocalDateTime.now(),  53.18999800151144, 8.750466937981729,
                    "thumbnailUrl9", "imageUrl9", "user9"));
            locationRepository.save(new Location("Pascos", "butze",
                    LocalDateTime.now(),  53.23274086211193, 8.784696922468369,
                    "thumbnailUrl10", "imageUrl10", "user10"));
            locationRepository.save(new Location("Lukasz", "bude",
                    LocalDateTime.now(),  53.21983113606297, 8.789933424275779,
                    "thumbnailUrl11", "imageUrl11", "user11"));
            locationRepository.save(new Location("Artems", "zelt",
                    LocalDateTime.now(),  53.219985308236474, 8.778990011495017,
                    "thumbnailUrl12", "imageUrl12", "user12"));
            locationRepository.save(new Location("standalone", "descr",
                    LocalDateTime.now(),  53.196894147862515, 8.61260156196291,
                    "thumbnailUrl13", "imageUrl13", "user13"));
            locationRepository.save(new Location("magemall", "stall",
                    LocalDateTime.now(),  53.195043047897364, 8.599898620068377,
                    "thumbnailUrl14", "imageUrl14", "user14"));
            locationRepository.save(new Location("beckedorf", "torf",
                    LocalDateTime.now(),  53.19667599779441, 8.598824600121679,
                    "thumbnailUrl15", "imageUrl15", "user15"));
            locationRepository.save(new Location("beckedorfsolo", "engagement",
                    LocalDateTime.now(),  53.2064054841533, 8.590285582959002,
                    "thumbnailUrl16", "imageUrl16", "user16"));
        };
    }
}
