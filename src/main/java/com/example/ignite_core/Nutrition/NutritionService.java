package com.example.ignite_core.Nutrition;

import com.example.ignite_core.Nutrition.Model.Entity.EatingHabitEntity;
import com.example.ignite_core.Nutrition.Model.Entity.MealBoxEntity;
import com.example.ignite_core.Nutrition.Model.Entity.MealEntity;
import com.example.ignite_core.Nutrition.Repository.EatingHabitRepository;
import com.example.ignite_core.Nutrition.Repository.MealBoxRepository;
import com.example.ignite_core.Nutrition.Repository.MealRepository;
import com.example.ignite_core.User.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.ignite_core.Utlility.ValidateTimes.validateTimes;

@Service
public class NutritionService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionService.class);

    EatingHabitRepository eatingHabitRepository;
    MealBoxRepository mealBoxRepository;
    UserRepository userRepository;
    MealRepository mealRepository;

    public NutritionService(EatingHabitRepository eatingHabitRepository, MealBoxRepository mealBoxRepository, UserRepository userRepository, MealRepository mealRepository) {
        this.eatingHabitRepository = eatingHabitRepository;
        this.mealBoxRepository = mealBoxRepository;
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
    }

    //Eating Habit
    public EatingHabitEntity addEatingHabits(EatingHabitEntity eatingHabitEntity) {
        if(!userRepository.existsById(eatingHabitEntity.getUserId())){
            throw new RuntimeException("User not found with id: " + eatingHabitEntity.getUserId());
        }

        if (eatingHabitRepository.existsByUserId(eatingHabitEntity.getUserId())) {
            throw new RuntimeException("Eating Habit is already exists for user with id: " + eatingHabitEntity.getUserId());
        }
        logger.info("Adding new Eating Habit with user id: {}", eatingHabitEntity.getUserId());
        return eatingHabitRepository.save(eatingHabitEntity);
    }

    public EatingHabitEntity updateEatingHabit(EatingHabitEntity eatingHabitEntity) {

        if(!eatingHabitRepository.existsByUserId(eatingHabitEntity.getUserId())){
            throw new RuntimeException("Eating Habit not found with user id: " + eatingHabitEntity.getUserId());
        }
        EatingHabitEntity existingEatingHabit = existsEatingHabitById(eatingHabitEntity.getId());
        existingEatingHabit.setFoodSource(eatingHabitEntity.getFoodSource());
        existingEatingHabit.setMealsPerDay(eatingHabitEntity.getMealsPerDay());
        existingEatingHabit.setEatingHabit(eatingHabitEntity.getEatingHabit());
        logger.info("Updating Eating Habit with id: {}", eatingHabitEntity.getId());
        return eatingHabitRepository.save(existingEatingHabit);
    }

    public EatingHabitEntity existsEatingHabitById(Long id){
        return eatingHabitRepository.findById(id).orElseThrow(() -> new RuntimeException("Eating Habit not found with id: " + id));
    }

    public List<EatingHabitEntity> getAllEatingHabits(){
        return eatingHabitRepository.findAll();
    }

    public Optional<EatingHabitEntity> getEatingHabitById(Long id){
        logger.info("Fetching Eating Habit with id: {}", id);
        return eatingHabitRepository.findById(id);
    }

    public Optional<EatingHabitEntity> getEatingHabitByUserId(Long userId){
        logger.info("Fetching eating habit by user id: {}", userId);
        return eatingHabitRepository.findByUserId(userId);
    }

    public void deleteEatingHabitById(Long id){
        EatingHabitEntity isEatingHabitExists = existsEatingHabitById(id);

        if(isEatingHabitExists == null){
            logger.error("Eating Habit not found with id: {}", id);
            throw new RuntimeException("Eating Habit not found with id: " + id);
        }
        eatingHabitRepository.deleteById(id);
    }

    //MealBox
    public MealBoxEntity saveMealBox(MealBoxEntity mealBox){
        if(!userRepository.existsById(mealBox.getUserId())){
            logger.error("User not found with id: {}", mealBox.getUserId());
            throw new RuntimeException("User not found with id: " + mealBox.getUserId());
        }
        if (mealBoxRepository.existsByUserId(mealBox.getUserId())) {
            logger.error("User not found with id: {}", mealBox.getUserId());
            throw new RuntimeException("Meal Box already exists for user with id: " + mealBox.getUserId());
        }

        //Validate Times
        mealBox.getMeals().forEach(meal -> validateTimes(meal.getStartDate(),meal.getEndDate()));

        for (MealEntity meal : mealBox.getMeals()) {
            meal.setMealBox(mealBox);
        }


        return mealBoxRepository.save(mealBox);
    }

    public List<MealEntity> saveMeals(List<MealEntity> meal){
        return mealRepository.saveAll(meal);
    }

    public List<MealBoxEntity> getAllMealBoxes(){
        try {
            logger.info("Fetching all meal boxes");
            return mealBoxRepository.findAll();
        } catch (Exception e) {
            // Log the exception
            logger.error("Error occurred while fetching meal boxes", e);
            // Optionally, you can throw a custom exception or return an empty list
            throw new RuntimeException("Unable to fetch meal boxes. Please try again later.");
        }
    }

    public Optional<MealBoxEntity> getMealBoxById(Long id){
        logger.info("Fetching meal box by id: {}", id);
        return mealBoxRepository.findById(id);
    }

    public Optional<MealBoxEntity> getMealBoxByUserId(Long userId){
        logger.info("Fetching meal box by user id: {}", userId);
        return mealBoxRepository.findByUserId(userId);
    }

    public MealBoxEntity existingMealBox(Long id){
        return mealBoxRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deleteMealBoxById(Long id) {
        // Assuming you want to remove MealBoxEntity and its associated MealEntities
        MealBoxEntity mealBox = mealBoxRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("MealBox not found"));

        for (MealEntity meal : mealBox.getMeals()) {
            mealRepository.deleteById(meal.getId());
        };

        mealBoxRepository.delete(mealBox);
    }


    //Meal

    //getAllMeals
    public List<MealEntity> getAllMeals(){
        try {
            return mealRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Failed to fetch meals from the database", e);
            throw new RuntimeException("Could not retrieve meals. Please try again later.", e);
        }
    }
    //getMealById
    public List<MealEntity> getMealByUserId(Long userId){
        logger.info("Fetching meal by user id: {}", userId);

        if (mealBoxRepository.findByUserId(userId).isEmpty()) throw new RuntimeException("Meal not found with id: " + userId);
        Optional<MealBoxEntity> mealBox = mealBoxRepository.findByUserId(userId);

        return mealBox.get().getMeals();
    }

    //getMealByMealId
    public Optional<MealEntity> getMealById(Long id){
        logger.info("Fetching meal by id: {}", id);
        return mealRepository.findById(id);
    }

    //saveMeal
    public void saveMeal(MealEntity meal){
        if (meal.getMealBox().getId() == null){
            throw new RuntimeException("Meal has not related to meal box");
        }

        for (MealEntity mealState : meal.getMealBox().getMeals() ) {
            if (mealState.equals(meal)) {
                throw new RuntimeException("Meal has already been saved");
            }
        }

        MealBoxEntity mealBox = mealBoxRepository.findById(meal.getMealBox().getId()).orElse(null);

        assert mealBox != null;
        mealBox.getMeals().add(meal);

        mealBoxRepository.save(mealBox);
    }

    //updateMeal
    public MealEntity updateMeal(MealEntity meal){
        Optional<MealEntity> existingMeal = mealRepository.findById(meal.getId());
        Optional<MealBoxEntity> existingMealBox = mealBoxRepository.findById(meal.getMealBox().getId());

        if (existingMeal.isPresent() && existingMealBox.isPresent()) {
            existingMeal.get().setMealBox(existingMealBox.get());
            existingMeal.get().setMealCode(meal.getMealCode());
            existingMeal.get().setStartDate(meal.getStartDate());
            existingMeal.get().setEndDate(meal.getEndDate());
            existingMeal.get().setContent(meal.getContent());
            existingMeal.get().setActive(meal.isActive());
            existingMeal.get().setLabel(meal.getLabel());
            existingMeal.get().setCalories(meal.getCalories());
        }
        return existingMeal.get();
    }
    //updateDate
    //updateContent(code,content)




}
