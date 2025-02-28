package com.example.ignite_core.Nutrition.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "MealBox")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MealBoxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @OneToMany(mappedBy = "mealBox", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealEntity> meals;
}
