package com.example.ignite_core.WaterIntake;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface WaterIntakeRepository extends JpaRepository<WaterIntakeEntity, Long> {
    @Query("SELECT w FROM WaterIntakeEntity w WHERE w.userId = :userId AND w.date = :date")
    Optional<WaterIntakeEntity> findWaterIntakeByDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    @Modifying
    @Query("DELETE FROM WaterIntakeEntity w WHERE w.userId = :userId AND w.date = :date")
    void deleteWaterIntakeByDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}
