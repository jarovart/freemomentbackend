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
import java.util.List;
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
                    new Location("NordMauer", "berlinmauer", "an der berliner mauer", LocalDateTime.now(),
                                 LocalDateTime.now().plusHours(1),
                                 LocalDateTime.now().plusDays(1).plusHours(1),
                                 53.23994798679545, 8.795070648193361,
                                 "", List.of(), user));
            locationRepository.save(new Location("OstBärlin", "berlinbär", "berlinärberaddress", LocalDateTime.now(),
                                                 LocalDateTime.now().minusHours(1),
                                                 LocalDateTime.now().plusDays(1).minusHours(1),
                                                 53.22710439096767, 8.808116912841799,
                                                 "", List.of(), user));
            locationRepository.save(new Location("WestSide", "berlinstand", "20277 berliner stand", LocalDateTime.now(),
                                                 LocalDateTime.now().minusDays(1).minusHours(1),
                                                 LocalDateTime.now().plusMonths(1).plusHours(1),
                                                 53.22957066025697, 8.766231536865236,
                                                 "", List.of(), user));
            locationRepository.save(new Location("SüdStand", "berlinstand", "südberlinaddresse", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(1),
                                                 LocalDateTime.now().plusWeeks(1).plusHours(1),
                                                 53.20191976452034, 8.760223388671877,
                                                 "", List.of(), user));
            locationRepository.save(new Location("aldi", "standmauer", "aldiadresse", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(4),
                                                 LocalDateTime.now().plusWeeks(2).plusDays(2).plusHours(1),
                                                 53.195762929604136, 8.627364440380877,
                                                 "", List.of(), user));
            locationRepository.save(
                    new Location("schweinewede", "standbärchen", "schweinewedeadresse", LocalDateTime.now(),
                                 LocalDateTime.now().plusDays(2).plusHours(1),
                                 LocalDateTime.now().plusWeeks(3).plusHours(1),
                                 53.22393166002433, 8.598868651806658,
                                 "", List.of(), user));
            locationRepository.save(
                    new Location("eastcoast", "berlinsaft", "berliner saftladenadresse", LocalDateTime.now(),
                                 LocalDateTime.now().plusMonths(2).plusHours(1),
                                 LocalDateTime.now().plusMonths(3).plusWeeks(1).plusHours(1),
                                 53.22867195051402, 8.795378619754933,
                                 "", List.of(), user));
            locationRepository.save(new Location("labbers", "", "", LocalDateTime.now(),
                                                 LocalDateTime.now().minusDays(3),
                                                 LocalDateTime.now().plusYears(1).minusDays(3).minusHours(10),
                                                 53.22256785805446, 8.795135853907508,
                                                 "", List.of(), user));
            locationRepository.save(
                    new Location("rittersbude", "fleischerhaken", "fleischereiadresse", LocalDateTime.now(),
                                 LocalDateTime.now(),
                                 LocalDateTime.now().plusDays(6).plusHours(5),
                                 53.18999800151144, 8.750466937981729,
                                 "", List.of(), user));
            locationRepository.save(new Location("Pascos", "butze", "27711 ohz", LocalDateTime.now(),
                                                 LocalDateTime.now(),
                                                 LocalDateTime.now().plusMinutes(1),
                                                 53.23274086211193, 8.784696922468369,
                                                 "", List.of(), user));
            locationRepository.save(new Location("Lukasz",
                                                 "budeunendlichlangedescriptionweilisssoundmussoextralangebeschraibungreichtfüerstesten",
                                                 "27711 Osterholz-Scharmbeck, Karlstraße 8", LocalDateTime.now(),
                                                 LocalDateTime.now().minusYears(1),
                                                 LocalDateTime.now().plusYears(5).plusHours(1),
                                                 53.21983113606297, 8.789933424275779,
                                                 "", List.of(), user));
            locationRepository.save(new Location("Artems", "zelt", "27711 ohz schillingstraße 4", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(1),
                                                 LocalDateTime.now().plusYears(20).minusHours(18),
                                                 53.219985308236474, 8.778990011495017,
                                                 "", List.of(), user));
            locationRepository.save(new Location("standalone", "descr", "standaloneadresse", LocalDateTime.now(),
                                                 LocalDateTime.now().plusYears(2).plusHours(1),
                                                 LocalDateTime.now().plusYears(2).plusMonths(1).plusHours(1),
                                                 53.196894147862515, 8.61260156196291,
                                                 "", List.of(), user));
            locationRepository.save(new Location("magemall", "stall", "magemalladresse", LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(1),
                                                 LocalDateTime.now().plusWeeks(1).plusHours(1),
                                                 53.195043047897364, 8.599898620068377,
                                                 "", List.of(), user));
            locationRepository.save(new Location("beckedorf", "torf", "beckedorfstrasse 12", LocalDateTime.now(),
                                                 LocalDateTime.now().minusMonths(3),
                                                 LocalDateTime.now().plusWeeks(1).plusHours(1),
                                                 53.19667599779441, 8.598824600121679,
                                                 "", List.of(), user));
            locationRepository.save(
                    new Location("beckedorfsolo", "engagement", "beckedorfsolostrasse 12", LocalDateTime.now(),
                                 LocalDateTime.now().minusMinutes(15),
                                 LocalDateTime.now().plusSeconds(30),
                                 53.2064054841533, 8.590285582959002,
                                 "", List.of(), user));
            locationRepository.save(
                    new Location("platjenwerbe", "der boss", "27712 platjenwerbe drogenkartell 12", LocalDateTime.now(),
                                 LocalDateTime.now().plusHours(1),
                                 LocalDateTime.now().plusHours(1),
                                 53.183081223241345, 8.688584010620108,
                                 "", List.of(), user));
            locationRepository.save(
                    new Location("platjenwerbes2", "der boZz", "der bozz strasse 12", LocalDateTime.now(),
                                 LocalDateTime.now().minusDays(1),
                                 LocalDateTime.now().plusDays(3).plusHours(1),
                                 53.183171234221916, 8.68324105026236,
                                 "", List.of(), user));
            locationRepository.save(new Location("fussballfeld?", "was geht in platjenwerbe", "fussbalfeldstrasse 12",
                                                 LocalDateTime.now(),
                                                 LocalDateTime.now().plusHours(1),
                                                 LocalDateTime.now().plusDays(2).plusHours(1),
                                                 53.179334469219405, 8.681879887658612,
                                                 "", List.of(), user));

        };
    }
}
