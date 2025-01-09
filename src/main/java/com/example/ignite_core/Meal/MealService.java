package com.example.ignite_core.Meal;

import com.example.ignite_core.Meal.Model.Entity.EatingHabitEntity;
import com.example.ignite_core.Meal.Model.Entity.MealBoxEntity;
import com.example.ignite_core.Meal.Model.Entity.MealEntity;
import com.example.ignite_core.Meal.Repository.EatingHabitRepository;
import com.example.ignite_core.Meal.Repository.MealBoxRepository;
import com.example.ignite_core.Meal.Repository.MealRepository;
import com.example.ignite_core.User.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    private static final Logger logger = LoggerFactory.getLogger(MealService.class);

    EatingHabitRepository eatingHabitRepository;
    MealBoxRepository mealBoxRepository;
    UserRepository userRepository;
    MealRepository mealRepository;

    public MealService(EatingHabitRepository eatingHabitRepository, MealBoxRepository mealBoxRepository, UserRepository userRepository, MealRepository mealRepository) {
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
    @Transactional
    public MealBoxEntity saveMealBox(MealBoxEntity mealBoxEntity){
        if(!userRepository.existsById(mealBoxEntity.getUserId())){
            logger.error("User not found with id: {}", mealBoxEntity.getUserId());
            throw new RuntimeException("User not found with id: " + mealBoxEntity.getUserId());
        }
        if (mealBoxRepository.existsByUserId(mealBoxEntity.getUserId())) {
            logger.error("User not found with id: {}", mealBoxEntity.getUserId());
            throw new RuntimeException("Meal Box already exists for user with id: " + mealBoxEntity.getUserId());
        }

        return mealBoxRepository.save(mealBoxEntity);
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

    public MealBoxEntity updateMealBox(MealBoxEntity mealBoxEntity){
        if(!userRepository.existsById(mealBoxEntity.getUserId())){
            logger.error("User not found to update with user id: {}", mealBoxEntity.getUserId());
            throw new RuntimeException("User not found to update with user id: " + mealBoxEntity.getUserId());
        }

        MealBoxEntity mealBox = existingMealBox(mealBoxEntity.getId());

        mealBox.setMeals(mealBoxEntity.getMeals()); //must be detailed
        logger.info("Updating Meal Box with id: {}", mealBoxEntity.getId());
        return mealBoxRepository.save(mealBox);
    }

    public MealBoxEntity existingMealBox(Long id){
        return mealBoxRepository.findById(id).orElse(null);
    }

    public void deleteMealBoxById(Long id){
        if (!userRepository.existsById(id)) {
            logger.error("User not found to delete with id: {}", id);
            throw new RuntimeException("User not found to delete with id: " + id);
        }
         mealBoxRepository.deleteById(id);
    }


    //Meal




}
