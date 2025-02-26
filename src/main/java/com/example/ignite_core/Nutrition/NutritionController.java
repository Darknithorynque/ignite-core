package com.example.ignite_core.Nutrition;

import com.example.ignite_core.Nutrition.Model.Entity.EatingHabitEntity;
import com.example.ignite_core.Nutrition.Model.Entity.MealBoxEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/nutrition")
public class NutritionController {

    NutritionService mealService;

    public NutritionController(NutritionService mealService) {
        this.mealService = mealService;
    }

//    @GetMapping("/eatingHabit/all")
//    public List<EatingHabitEntity> getAllEatingHabits() {
//        return mealService.getAllEatingHabits();
//    }
//
//    @GetMapping("/eatingHabit/{id}")
//    public Optional<EatingHabitEntity> getEatingHabitById(@PathVariable Long id) {
//        return mealService.getEatingHabitById(id);
//    }
//
//    @GetMapping("/eatingHabit/user/{userId}")
//    public Optional<EatingHabitEntity> getEatingHabitByUserId(@PathVariable Long userId) {
//        return mealService.getEatingHabitByUserId(userId);
//    }
//
//    @PostMapping("/eatingHabit/save")
//    public EatingHabitEntity saveEatingHabit(@RequestBody EatingHabitEntity entity) {
//        return mealService.addEatingHabits(entity);
//    }
//
//    @PutMapping("/eatingHabit/update")
//    public EatingHabitEntity updateEatingHabit(@RequestBody EatingHabitEntity entity) {
//        return mealService.updateEatingHabit(entity);
//    }
//
//    @DeleteMapping("/eatingHabit/delete/{id}")
//    public void deleteEatingHabit(@PathVariable Long id) {
//        mealService.deleteEatingHabitById(id);
//    }

    //MealBox
    @PostMapping("/mealBox/save")
    public MealBoxEntity saveMealBox(@RequestBody MealBoxEntity mealBox) {
        return mealService.saveMealBox(mealBox);
    }

    @GetMapping("/mealBox/all")
    public List<MealBoxEntity> getAllMealBoxes() {
        return mealService.getAllMealBoxes();
    }

    @GetMapping("/mealBox/{id}")
    public Optional<MealBoxEntity> getMealBoxById(@PathVariable Long id) {
        return mealService.getMealBoxById(id);
    }

    @GetMapping("/mealBox/user/{userId}")
    public Optional<MealBoxEntity> getMealBoxByUserId(@PathVariable Long userId) {
        return mealService.getMealBoxByUserId(userId);
    }

    @DeleteMapping("/mealBox/delete/{id}")
    public void deleteMealBox(@PathVariable Long id) {
        mealService.deleteMealBoxById(id);
    }
}
