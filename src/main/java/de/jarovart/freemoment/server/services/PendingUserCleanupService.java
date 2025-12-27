package de.jarovart.freemoment.server.services;

import de.jarovart.freemoment.server.repository.PendingUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PendingUserCleanupService {

    private final PendingUserRepository pendingUserRepository;

    @Scheduled(cron = "0 0 3 * * *") // täglich um 03:00 Uhr
    public void cleanupExpiredPendingUsers() {
        int deleted = pendingUserRepository.deleteExpired(LocalDateTime.now());
        log.info("🧹 Deleted {} expired PendingUsers", deleted);
    }
}
