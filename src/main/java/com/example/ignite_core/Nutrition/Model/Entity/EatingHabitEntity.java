package com.example.ignite_core.Nutrition.Model.Entity;

import com.example.ignite_core.Nutrition.Model.Enum.FoodSource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "EatingHabit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EatingHabitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int mealsPerDay;

    private String eatingHabit;

    private FoodSource foodSource;

    private Long userId;

}
