package com.example.ignite_core.Nutrition.Model.Entity;


import com.example.ignite_core.Nutrition.Model.Enum.Label;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime endDate;

    private Calories calories;

    private Label label;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "meal_box_id")
    @JsonIgnore
    private MealBoxEntity mealBox;

    @Override
    public String toString(){
        return createdAt + " " + endDate;
    }
}
