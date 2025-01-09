package com.example.ignite_core.Meal.Model.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Table(name = "Meal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int mealCode; //1: Breakfast, 2: Lunch, 3: Dinner, 4: Snack

    private String content;

    private LocalDate startDate;

    private LocalDate endDate;

    private Calories calories;

    @ManyToOne
    @JoinColumn(name = "meal_box_id")
    private MealBoxEntity mealBox;

    @Override
    public String toString(){
        return startDate + " " + endDate;
    }
}
