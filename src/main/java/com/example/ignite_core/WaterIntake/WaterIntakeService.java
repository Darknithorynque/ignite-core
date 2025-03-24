package com.example.ignite_core.WaterIntake;

import com.example.ignite_core.User.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WaterIntakeService {

    private final WaterIntakeRepository repository;

    private final Logger logger = LoggerFactory.getLogger(WaterIntakeService.class);

    public WaterIntakeService(WaterIntakeRepository repository, UserRepository userRepository) {
        this.repository = repository;
    }

    @Transactional
    ResponseEntity<WaterIntakeEntity> logWaterIntake(Long userId, double amount) {
        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();
        WaterIntakeEntity waterIntake = repository.findWaterIntakeByDate(userId, today)
                .orElseGet(() -> repository.save(new WaterIntakeEntity(userId, today, 0.0, now)));


        waterIntake.setAmount(amount);
        //waterIntake.setLastUpdatedAt(now);
        waterIntake.setUserId(userId);
        //waterIntake.setDate(today);

        repository.save(waterIntake);
        return ResponseEntity.ok(waterIntake);
    }

    ResponseEntity<Optional<WaterIntakeEntity>> findWaterIntakeByDate(Long userId, LocalDate date) {

        try {
            if (!date.isAfter(LocalDate.now())) {
                Optional<WaterIntakeEntity> waterIntake = repository.findWaterIntakeByDate(userId, date);
                return ResponseEntity.ok(waterIntake);
            } else {
                logger.warn("WaterIntake date {} not available", date);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Optional.empty());
        }
    }

    ResponseEntity<WaterIntakeEntity> updateWaterIntake(Long userId, double amount, LocalDate date) {
        try {
            LocalDateTime now = LocalDateTime.now();

            if (date.isAfter(LocalDate.now())) {
                logger.warn("No water intake record found for user {} on {}", userId, date);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            Optional<WaterIntakeEntity> waterIntake = repository.findByUserId(userId);

            waterIntake.ifPresent(intake -> {
                intake.setAmount(amount);
                intake.setLastUpdatedAt(now);
                repository.save(intake);
                logger.info("Updated water intake for user {} on {}", userId, date);
            });

            return ResponseEntity.status(waterIntake.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND).build();

        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Transactional
    ResponseEntity<String> deleteWaterIntake(Long userId, LocalDate date) {
        Optional<WaterIntakeEntity> existingWaterIntake = repository.findWaterIntakeByDate(userId, date);

        if (existingWaterIntake.isPresent()){
            logger.info("deleted waterIntake with date and userId: {} {}", date, userId);
            repository.deleteWaterIntakeByDate(userId, date);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted");
        } else {
            logger.warn("No record found to delete");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}