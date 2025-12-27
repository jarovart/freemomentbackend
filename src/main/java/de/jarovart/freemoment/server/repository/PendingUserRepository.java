package de.jarovart.freemoment.server.repository;

import de.jarovart.freemoment.server.model.entities.PendingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PendingUserRepository extends JpaRepository<PendingUser, Long> {

    @Modifying
    @Query("DELETE FROM PendingUser p WHERE p.expiresAt < :now")
    int deleteExpired(@Param("now") LocalDateTime now);


    public Optional<PendingUser> findByEmail(String email);

    public Optional<PendingUser> findByVerifyToken(String token);
}

