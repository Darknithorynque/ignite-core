package com.example.ignite_core.StepTracking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface StepTrackingRepository extends JpaRepository<StepTrackingEntity, Long> {
    Optional<StepTrackingEntity> findByUserIdAndDate(Long userId, LocalDate date);
}
