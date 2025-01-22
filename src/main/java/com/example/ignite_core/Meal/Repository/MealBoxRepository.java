package com.example.ignite_core.Meal.Repository;

import com.example.ignite_core.Meal.Model.Entity.MealBoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealBoxRepository extends JpaRepository<MealBoxEntity, Long> {
    Boolean existsByUserId(Long userId);
    Optional<MealBoxEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
