package com.example.ignite_core.StepTracking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StepTrackingService {
    private final StepTrackingRepository repository;
    private final Map<Long, Integer> stepCache = new ConcurrentHashMap<>();

    Logger logger = LoggerFactory.getLogger(StepTrackingService.class);

    public StepTrackingService(StepTrackingRepository repository) {
        this.repository = repository;
    }

    public void updateStep(Long userId, int steps) {
        stepCache.put(userId, steps);
    }

    public ResponseEntity<StepTrackingEntity> updateRecord(Long userId, int steps, LocalDate date) {

        Optional<StepTrackingEntity> existingRecord = repository.findByUserIdAndDate(userId,date);

        existingRecord.ifPresent(record -> {
            record.setStepCount(steps);
            repository.save(record);
        });

        if (existingRecord.isEmpty()) {
            logger.warn("No record found for user id {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
       StepTrackingEntity body = repository.save(new StepTrackingEntity(userId, steps, LocalDate.now()));
        logger.info("Updated record for user id {}", userId);
        return ResponseEntity.ok(body);
    }

    public int getSteps(Long userId) {
        return stepCache.getOrDefault(userId, 0);
    }

    @Scheduled(cron = "59 59 23 * * ?") // Runs every day at 23:59
    public void saveDailySteps() {
        LocalDate today = LocalDate.now();

        stepCache.forEach((userId, steps) -> {
            StepTrackingEntity record = repository
                    .findByUserIdAndDate(userId, today)
                    .orElse(new StepTrackingEntity(userId, 0, today));

            record.setStepCount(steps);
            repository.save(record);
        });

        stepCache.clear();
    }
}
