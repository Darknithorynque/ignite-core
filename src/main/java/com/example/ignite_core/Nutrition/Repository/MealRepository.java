package com.example.ignite_core.Nutrition.Repository;

import com.example.ignite_core.Nutrition.Model.Entity.MealEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MealRepository extends JpaRepository<MealEntity,Long> {

    @Modifying
    @Query("UPDATE MealEntity m SET m.inBoxStatus = :inBoxStatus WHERE m.id = :mealId")
    void updateInBoxStatus(@Param("mealId") Long mealId, @Param("inBoxStatus") boolean inBoxStatus);
}
