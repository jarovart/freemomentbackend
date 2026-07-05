/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package de.jarovart.freemoment.server.configurations;

import de.jarovart.freemoment.server.model.entities.AppUser;
import de.jarovart.freemoment.server.model.entities.Image;
import de.jarovart.freemoment.server.model.entities.Location;
import de.jarovart.freemoment.server.model.enums.UserRole;
import de.jarovart.freemoment.server.repository.ImageRepository;
import de.jarovart.freemoment.server.repository.LocationRepository;
import de.jarovart.freemoment.server.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Artem
 */
@Profile({"dev", "staging"})
@Configuration
public class DbSeeding {

    private final ImageRepository imageRepository;
    private final LocationRepository locationRepository;

    public DbSeeding(ImageRepository imageRepository, LocationRepository locationRepository) {
        this.imageRepository = imageRepository;
        this.locationRepository = locationRepository;
    }

    @Bean
    public CommandLineRunner init(LocationRepository locationRepository,
                                  UserRepository userRepository,
                                  ImageRepository imageRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("jarovart").isPresent()) {
                return;
            }

            String image5b = "44c58561-5513-49b8-b539-4ee6d1644c5b.jpg";
            String image67 = "e42aab1e-880c-427b-8fd9-ed9018533167.jpg";
            String image81 = "7a3a4873-b5ba-40b4-bccd-80c36fbd6b81.jpg";
            String imagecc = "74ae79bb-f1b0-4653-8df8-90cbd138b0cc.jpg";

            AppUser user = createUser(
                    "jarovart",
                    "Artem",
                    "Jarovoj",
                    "info@jarovart.de",
                    "about me? i am the admin",
                    "test",
                    passwordEncoder,
                    true
            );
            user = userRepository.save(user);

            Image profileImage = createImage(user, imagecc);
            profileImage = imageRepository.save(profileImage);

            user.setProfileImage(profileImage);
            user = userRepository.save(user);

            Image profileImage1 = createImage(user, imagecc);
            profileImage1 = imageRepository.save(profileImage1);
            List<AppUser> userList = createSeedingUsers(100, profileImage1, passwordEncoder);
            userList = userRepository.saveAll(userList);

            AppUser user1 = createUser(
                    "petervart",
                    "Petertem",
                    "blackjaroblack",
                    "info2@jarovart.de",
                    "what should I tell u?",
                    "test123",
                    passwordEncoder,
                    false
            );
            user1 = userRepository.save(user1);


            createLocation(
                    "NordMauer",
                    "berlinmauer",
                    "an der berliner mauer",
                    LocalDateTime.now().plusHours(1),
                    LocalDateTime.now().plusDays(1).plusHours(1),
                    53.23994798679545,
                    8.795070648193361,
                    image5b,
                    List.of(image67),
                    user
            );


            createLocation("NordMauer", "berlinmauer", "an der berliner mauer",
                           LocalDateTime.now().plusHours(1),
                           LocalDateTime.now().plusDays(1).plusHours(1),
                           53.23994798679545, 8.795070648193361,
                           image5b, List.of(image67), user);
            createLocation("OstBärlin", "berlinbär", "berlinärberaddress",
                           LocalDateTime.now().minusHours(1),
                           LocalDateTime.now().plusDays(1).minusHours(1),
                           53.22710439096767, 8.808116912841799,
                           image5b, List.of(image67),
                           user);
            createLocation("WestSide", "berlinstand", "20277 berliner stand",
                           LocalDateTime.now().minusDays(1).minusHours(1),
                           LocalDateTime.now().plusMonths(1).plusHours(1),
                           53.22957066025697, 8.766231536865236,
                           image5b, List.of(image67),
                           user);
            createLocation("SüdStand", "berlinstand", "südberlinaddresse",
                           LocalDateTime.now().plusHours(1),
                           LocalDateTime.now().plusWeeks(1).plusHours(1),
                           53.20191976452034, 8.760223388671877,
                           image5b, List.of(image67),
                           user);
            createLocation("aldi", "standmauer", "aldiadresse",
                           LocalDateTime.now().plusHours(4),
                           LocalDateTime.now().plusWeeks(2).plusDays(2).plusHours(1),
                           53.195762929604136, 8.627364440380877,
                           image5b, List.of(image67, image81),
                           user);
            createLocation("schweinewede", "standbärchen", "schweinewedeadresse",
                           LocalDateTime.now().plusDays(2).plusHours(1),
                           LocalDateTime.now().plusWeeks(3).plusHours(1),
                           53.22393166002433, 8.598868651806658,
                           image5b, List.of(image67, imagecc), user);
            createLocation("eastcoast", "berlinsaft", "berliner saftladenadresse",
                           LocalDateTime.now().plusMonths(2).plusHours(1),
                           LocalDateTime.now().plusMonths(3).plusWeeks(1).plusHours(1),
                           53.22867195051402, 8.795378619754933,
                           image67, List.of(image5b), user);
            createLocation("labbers", "", "",
                           LocalDateTime.now().minusDays(3),
                           LocalDateTime.now().plusYears(1).minusDays(3).minusHours(10),
                           53.22256785805446, 8.795135853907508,
                           image67, List.of(image5b),
                           user);
            createLocation("rittersbude", "fleischerhaken", "fleischereiadresse",
                           LocalDateTime.now(),
                           LocalDateTime.now().plusDays(6).plusHours(5),
                           53.18999800151144, 8.750466937981729,
                           image67, List.of(image5b), user);
            createLocation("Pascos", "butze", "27711 ohz",
                           LocalDateTime.now(),
                           LocalDateTime.now().plusMinutes(1),
                           53.23274086211193, 8.784696922468369,
                           image67, List.of(image5b),
                           user);
            createLocation("Lukasz",
                           "budeunendlichlangedescriptionweilisssoundmussoextralangebeschraibungreichtfüerstesten",
                           "Deutschland, 27711 Osterholz-Scharmbeck, Karlstraße 8",
                           LocalDateTime.now().minusYears(1),
                           LocalDateTime.now().plusYears(5).plusHours(1),
                           53.21983113606297, 8.789933424275779,
                           image67, List.of(image81, imagecc, image5b),
                           user);
            createLocation("Artems", "zelt", "27711 ohz schillingstraße 4",
                           LocalDateTime.now().plusHours(1),
                           LocalDateTime.now().plusYears(20).minusHours(18),
                           53.219985308236474, 8.778990011495017,
                           image67, List.of(image5b, image81, imagecc),
                           user);
            createLocation("standalone", "descr", "standaloneadresse",
                           LocalDateTime.now().plusYears(2).plusHours(1),
                           LocalDateTime.now().plusYears(2).plusMonths(1).plusHours(1),
                           53.196894147862515, 8.61260156196291,
                           "", List.of(), user);
            createLocation("magemall", "stall", "magemalladresse",
                           LocalDateTime.now().plusHours(1),
                           LocalDateTime.now().plusWeeks(1).plusHours(1),
                           53.195043047897364, 8.599898620068377,
                           "", List.of(), user);
            createLocation("beckedorf", "torf", "beckedorfstrasse 12",
                           LocalDateTime.now().minusMonths(3),
                           LocalDateTime.now().plusWeeks(1).plusHours(1),
                           53.19667599779441, 8.598824600121679,
                           "", List.of(), user);
            createLocation("beckedorfsolo", "engagement", "beckedorfsolostrasse 12",
                           LocalDateTime.now().minusMinutes(15),
                           LocalDateTime.now().plusSeconds(30),
                           53.2064054841533, 8.590285582959002,
                           image67, List.of(image5b), user);
            createLocation("platjenwerbe", "der boss", "27712 platjenwerbe drogenkartell 12",
                           LocalDateTime.now().plusHours(1),
                           LocalDateTime.now().plusHours(1),
                           53.183081223241345, 8.688584010620108,
                           image67, List.of(image5b, image81), user);
            createLocation("platjenwerbes2", "der boZz", "der bozz strasse 12",
                           LocalDateTime.now().minusDays(1),
                           LocalDateTime.now().plusDays(3).plusHours(1),
                           53.183171234221916, 8.68324105026236,
                           image67, List.of(image5b), user);
            createLocation("fussballfeld?", "was geht in platjenwerbe", "fussbalfeldstrasse 12",
                           LocalDateTime.now().plusHours(1),
                           LocalDateTime.now().plusDays(2).plusHours(1),
                           53.179334469219405, 8.681879887658612,
                           image67, List.of(image5b), user);
        };
    }

    private AppUser createUser(
            String username,
            String firstName,
            String lastName,
            String email,
            String aboutMe,
            String rawPassword,
            PasswordEncoder passwordEncoder,
            boolean isAdmin
    ) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setAboutMe(aboutMe);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setCreatedAt(LocalDateTime.now());
        if (isAdmin) {
            user.setRoles(Set.of(UserRole.ROLE_USER, UserRole.ROLE_ADMIN));
        } else {
            user.setRoles(Set.of(UserRole.ROLE_USER));
        }

        return user;
    }

    private Image createImage(AppUser user, String filename) {
        Image image = new Image();
        image.setUploadedByUser(user);
        image.setCreationDateTime(LocalDateTime.now());
        image.setFilename(filename);
        image.setSize(1);
        image.setContentType("image/jpeg");
        return image;
    }

    private Location createLocation(
            String title,
            String description,
            String address,
            LocalDateTime start,
            LocalDateTime end,
            double lat,
            double lng,
            String thumbnailUrl,
            List<String> imageUrls,
            AppUser createdUser
    ) {
        Location location = new Location();
        location.setTitle(title);
        location.setDescription(description);
        location.setAddress(address);
        location.setCreationDateTime(LocalDateTime.now());
        location.setStartDateTime(start);
        location.setEndDateTime(end);
        location.setLatitude(lat);
        location.setLongitude(lng);
        location.setCreatedUser(createdUser);

        List<Image> images = new ArrayList<>();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            Image thumbnailImage = createImage(createdUser, thumbnailUrl);
            thumbnailImage.setLocation(location);
            images.add(thumbnailImage);
        }

        for (String imageUrl : imageUrls) {
            Image image = createImage(createdUser, imageUrl);
            image.setLocation(location);
            images.add(image);
        }
        location.getImages().addAll(images);

        if (!images.isEmpty()) {
            location.setThumbnailImage(images.getFirst());
        }

        locationRepository.save(location);
        return location;
    }

    private List<AppUser> createSeedingUsers(int userSize, Image profileImage, PasswordEncoder passwordEncoder) {
        List<AppUser> userList = new ArrayList<>();
        for (int i = 0;
             i < userSize;
             i++) {
            AppUser user = createUser(
                    "jarovart" + i,
                    "Artem" + i,
                    "Jarovoj" + i,
                    "info@jarovart.de" + i,
                    "about me? i am the randomuser" + i,
                    "test",
                    passwordEncoder,
                    false
            );
            //user.setProfileImage(profileImage);
            userList.add(user);
        }
        return userList;
    }
}
