package com.example.ignite_core.Nutrition.Model.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealRequest {
    private Long userId;
    private String mealName;
}
