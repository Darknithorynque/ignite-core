package com.example.ignite_core.Nutrition.Model.Response;

import com.example.ignite_core.Nutrition.Model.Entity.Calories;
import com.example.ignite_core.Nutrition.Model.Enum.Label;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealResponse {
    private Long userId;
    private Label label;
    private Calories calories;
    private String mealName;
}
