package com.example.ignite_core.Nutrition.Repository;

import com.example.ignite_core.Nutrition.Model.Entity.EatingHabitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EatingHabitRepository extends JpaRepository<EatingHabitEntity, Long> {
    Optional<EatingHabitEntity> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
