package de.jarovart.freemoment.server.model.entities;

import de.jarovart.freemoment.server.model.enums.AppDesignEnum;
import de.jarovart.freemoment.server.model.enums.LanguageEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LanguageEnum language;

    @Enumerated(EnumType.STRING)
    private AppDesignEnum appDesign;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private AppUser user;

    private LocalDateTime updatedAt;

    //Need for DB
    public UserSetting() {
    }

    public UserSetting(LanguageEnum language, AppDesignEnum appDesign, LocalDateTime updatedAt, AppUser user) {
        this.language = language;
        this.appDesign = appDesign;
        this.updatedAt = updatedAt;
        this.user = user;
    }
}
