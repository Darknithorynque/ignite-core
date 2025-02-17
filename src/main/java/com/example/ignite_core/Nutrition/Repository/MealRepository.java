package com.example.ignite_core.Nutrition.Repository;

import com.example.ignite_core.Nutrition.Model.Entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<MealEntity,Long> {
}
