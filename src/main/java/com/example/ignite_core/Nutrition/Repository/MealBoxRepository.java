package com.example.ignite_core.Nutrition.Repository;

import com.example.ignite_core.Nutrition.Model.Entity.MealBoxEntity;
import com.example.ignite_core.Nutrition.Model.Entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealBoxRepository extends JpaRepository<MealBoxEntity, Long> {
    Boolean existsByUserId(Long userId);
    Optional<MealBoxEntity> findByUserId(Long userId);
    void deleteByUserId(Long userId);
    @Query("SELECT m FROM MealEntity m JOIN m.mealBox mb WHERE mb.userId = :userId AND m.inBoxStatus = true")
    List<MealEntity> findActiveMealsByUserId(@Param("userId") Long userId);
}
