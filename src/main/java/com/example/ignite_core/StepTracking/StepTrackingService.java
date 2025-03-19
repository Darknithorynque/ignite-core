package com.example.ignite_core.StepTracking;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StepTrackingService {
    private final StepTrackingRepository repository;
    private final Map<Long, Integer> stepCache = new ConcurrentHashMap<>();

    public StepTrackingService(StepTrackingRepository repository) {
        this.repository = repository;
    }

    public void updateStep(Long userId, int steps) {
        stepCache.put(userId, steps);
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
