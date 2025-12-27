/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.configurations;

import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.enums.UserRole;
import de.jarovart.freemoment.server.repository.LocationRepository;
import de.jarovart.freemoment.server.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

/**
 *
 * @author Artem
 */
@Configuration
public class DbSeeding {

    @Bean
    public CommandLineRunner init(LocationRepository locationRepository,
                                  UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {
            AppUser user = new AppUser();
            user.setUsername("jarovart");
            user.setEmail("info@jarovart.de");
            user.setPassword(passwordEncoder.encode("test"));
            user.setRoles(Set.of(UserRole.ROLE_USER));
            userRepository.save(user);
            locationRepository.save(
                    new Location("NordMauer", "berlinmauer", LocalDateTime.now(),
                                 LocalDateTime.now().plusHours(1),
                                 LocalDateTime.now().plusDays(1).plusHours(1),
                                 53.23994798679545, 8.795070648193361,
                                 "thumbnailUrl1", "imageUrl1", user));
            locationRepository.save(new Location("OstBärlin", "berlinbär", LocalDateTime.now(),
                                                 LocalDateTime.now().minusHours(1),
                                                 LocalDateTime.now().plusDays(1).minusHours(1),
                                                 53.22710439096767, 8.808116912841799,
                                                 "thumbnailUrl2", "imageUrl2", user));
            locationRepository.save(new Location("WestSide", "berlinstand", LocalDateTime.now(),
                                                 LocalDateTime.now().minusDays(1).minusHours(1),
                                                 LocalDateTime.now().plusMonths(1).plusHours(1),
                                                 53.22957066025697, 8.766231536865236,
                                                 "thumbnailUrl3", "imageUrl3", user));
            locationRepository.save(new Location("SüdStand", "berlinstand", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(1),
                                                 LocalDateTime.now().plusWeeks(1).plusHours(1),
                                                 53.20191976452034, 8.760223388671877,
                                                 "thumbnailUrl4", "imageUrl4", user));
            locationRepository.save(new Location("aldi", "standmauer", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(4),
                                                 LocalDateTime.now().plusWeeks(2).plusDays(2).plusHours(1),
                                                 53.195762929604136, 8.627364440380877,
                                                 "thumbnailUrl5", "imageUrl5", user));
            locationRepository.save(new Location("schweinewede", "standbärchen", LocalDateTime.now(),
                                                 LocalDateTime.now().plusDays(2).plusHours(1),
                                                 LocalDateTime.now().plusWeeks(3).plusHours(1),
                                                 53.22393166002433, 8.598868651806658,
                                                 "thumbnailUrl6", "imageUrl6", user));
            locationRepository.save(new Location("eastcoast", "berlinsaft", LocalDateTime.now(),
                                                 LocalDateTime.now().plusMonths(2).plusHours(1),
                                                 LocalDateTime.now().plusMonths(3).plusWeeks(1).plusHours(1),
                                                 53.22867195051402, 8.795378619754933,
                                                 "thumbnailUrl7", "imageUrl7", user));
            locationRepository.save(new Location("labbers", "", LocalDateTime.now(),
                                                 LocalDateTime.now().minusDays(3),
                                                 LocalDateTime.now().plusYears(1).minusDays(3).minusHours(10),
                                                 53.22256785805446, 8.795135853907508,
                                                 "thumbnailUrl8", "imageUrl8", user));
            locationRepository.save(new Location("rittersbude", "fleischerhaken", LocalDateTime.now(),
                                                 LocalDateTime.now(),
                                                 LocalDateTime.now().plusDays(6).plusHours(5),
                                                 53.18999800151144, 8.750466937981729,
                                                 "thumbnailUrl9", "imageUrl9", user));
            locationRepository.save(new Location("Pascos", "butze", LocalDateTime.now(),
                                                 LocalDateTime.now(),
                                                 LocalDateTime.now().plusMinutes(1),
                                                 53.23274086211193, 8.784696922468369,
                                                 "thumbnailUrl10", "imageUrl10", user));
            locationRepository.save(new Location("Lukasz",
                                                 "budeunendlichlangedescriptionweilisssoundmussoextralangebeschraibungreichtfüerstesten",
                                                 LocalDateTime.now(),
                                                 LocalDateTime.now().minusYears(1),
                                                 LocalDateTime.now().plusYears(5).plusHours(1),
                                                 53.21983113606297, 8.789933424275779,
                                                 "thumbnailUrl11", "imageUrl11", user));
            locationRepository.save(new Location("Artems", "zelt", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(1),
                                                 LocalDateTime.now().plusYears(20).minusHours(18),
                                                 53.219985308236474, 8.778990011495017,
                                                 "thumbnailUrl12", "imageUrl12", user));
            locationRepository.save(new Location("standalone", "descr", LocalDateTime.now(),
                                                 LocalDateTime.now().plusYears(2).plusHours(1),
                                                 LocalDateTime.now().plusYears(2).plusMonths(1).plusHours(1),
                                                 53.196894147862515, 8.61260156196291,
                                                 "thumbnailUrl13", "imageUrl13", user));
            locationRepository.save(new Location("magemall", "stall", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(1),
                                                 LocalDateTime.now().plusWeeks(1).plusHours(1),
                                                 53.195043047897364, 8.599898620068377,
                                                 "thumbnailUrl14", "imageUrl14", user));
            locationRepository.save(new Location("beckedorf", "torf", LocalDateTime.now(),
                                                 LocalDateTime.now().minusMonths(3),
                                                 LocalDateTime.now().plusWeeks(1).plusHours(1),
                                                 53.19667599779441, 8.598824600121679,
                                                 "thumbnailUrl15", "imageUrl15", user));
            locationRepository.save(new Location("beckedorfsolo", "engagement", LocalDateTime.now(),
                                                 LocalDateTime.now().minusMinutes(15),
                                                 LocalDateTime.now().plusSeconds(30),
                                                 53.2064054841533, 8.590285582959002,
                                                 "thumbnailUrl16", "imageUrl16", user));
            locationRepository.save(new Location("platjenwerbe", "der boss", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(1),
                                                 LocalDateTime.now().plusHours(1),
                                                 53.183081223241345, 8.688584010620108,
                                                 "thumbnailUrl17", "imageUrl17", user));
            locationRepository.save(new Location("platjenwerbes2", "der boZz", LocalDateTime.now(),
                                                 LocalDateTime.now().minusDays(1),
                                                 LocalDateTime.now().plusDays(3).plusHours(1),
                                                 53.183171234221916, 8.68324105026236,
                                                 "thumbnailUrl18", "imageUrl18", user));
            locationRepository.save(new Location("fussballfeld?", "was geht in platjenwerbe", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(1),
                                                 LocalDateTime.now().plusDays(2).plusHours(1),
                                                 53.179334469219405, 8.681879887658612,
                                                 "thumbnailUrl19", "imageUrl19", user));

        };
    }
}
