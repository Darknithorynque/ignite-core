package com.example.ignite_core.Nutrition.Service;

import com.example.ignite_core.Nutrition.Model.Entity.MealBoxEntity;
import com.example.ignite_core.Nutrition.Model.Entity.MealEntity;
import com.example.ignite_core.Nutrition.Repository.MealBoxRepository;
import com.example.ignite_core.Nutrition.Repository.MealRepository;
import com.example.ignite_core.User.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.ignite_core.Utlility.ValidateTimes.validateTimes;

@Service
public class NutritionService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionService.class);

    MealBoxRepository mealBoxRepository;
    UserRepository userRepository;
    MealRepository mealRepository;


    public NutritionService(MealBoxRepository mealBoxRepository, UserRepository userRepository, MealRepository mealRepository) {
        this.mealBoxRepository = mealBoxRepository;
        this.userRepository = userRepository;
        this.mealRepository = mealRepository;
    }

    //MealBox
    public MealBoxEntity saveMealBox(MealBoxEntity mealBox){
        if(!userRepository.existsById(mealBox.getUserId())){
            logger.error("User not found with user id: {}", mealBox.getUserId());
            throw new RuntimeException("User not found with id: " + mealBox.getUserId());
        }
        if (mealBoxRepository.existsByUserId(mealBox.getUserId())) {
            logger.error("MealBox not found with user id: {}", mealBox.getUserId());
            throw new RuntimeException("Meal Box already exists for user with id: " + mealBox.getUserId());
        }

        //Validate Times
        mealBox.getMeals().forEach(meal -> validateTimes(meal.getCreatedAt(),meal.getEndDate()));

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

    public ResponseEntity<List<MealEntity>> findActiveMealsByUserId(Long userId){
        List<MealEntity> body = mealBoxRepository.findActiveMealsByUserId(userId);
        return ResponseEntity.ok(body);

    }

    @Transactional
    public void updateInMealBoxStatus(Long mealId, boolean inBoxStatus){
        mealRepository.updateInBoxStatus(mealId, inBoxStatus);

        ResponseEntity.ok("Updated status as {} " + inBoxStatus);
    }

    //Meal

    //getAllMeals
    public List<MealEntity> getAllMeals(){
        try {
            logger.info("Fetching all meals");
            return mealRepository.findAll();
        } catch (DataAccessException e) {
            logger.error("Failed to fetch meals from the database", e);
            throw new RuntimeException("Could not retrieve meals. Please try again later.", e);
        }
    }
    //getMealById
    public List<MealEntity> getMealByUserId(Long userId){
        logger.info("Fetching meal by user id: {}", userId);

        if (mealBoxRepository.findByUserId(userId).isEmpty()){
            logger.error("Meal not found with user id {}", userId);
            throw new RuntimeException("Meal not found with id: " + userId);
        }

        Optional<MealBoxEntity> mealBox = mealBoxRepository.findByUserId(userId);

        return mealBox.get().getMeals();
    }

    //getMealByMealId
    public ResponseEntity<Optional<MealEntity>> getMealById(Long id){
        logger.info("Fetching meal by id: {}", id);
        return ResponseEntity.ok(mealRepository.findById(id));
    }

    //saveMeal
    public ResponseEntity<MealEntity> saveMeal(MealEntity meal, Long userId){
        MealBoxEntity mealBox = mealBoxRepository.findByUserId(userId).orElse(null);

        if (mealBox == null) {
            logger.error("Related meal box has not been found");
            throw new RuntimeException("User has not related to a meal box");
        }

        for (MealEntity mealState : mealBox.getMeals()) {
            if (mealState.equals(meal)) {
                logger.error("Meal already has in the meal box");
                throw new RuntimeException("Meal has already been saved");
            }
        }

        assert mealBox != null;
        mealBox.getMeals().add(meal);
        meal.setMealBox(mealBox);

        logger.info("Saving meal: {}", meal);
        mealBoxRepository.save(mealBox);
        return ResponseEntity.ok(meal);
    }

    //updateMeal
    public ResponseEntity<MealEntity> updateMeal(MealEntity meal){
        Optional<MealEntity> existingMeal = mealRepository.findById(meal.getId());
        Optional<MealBoxEntity> existingMealBox = mealBoxRepository.findByUserId(existingMeal.get().getMealBox().getUserId());

        logger.info("Updating meal: {}", meal);
        if (existingMeal.isPresent() && existingMealBox.isPresent()) {
            existingMeal.get().setMealBox(existingMealBox.get());
            existingMeal.get().setCreatedAt(meal.getCreatedAt());
            existingMeal.get().setEndDate(meal.getEndDate());
            existingMeal.get().setContent(meal.getContent());
            existingMeal.get().setInBoxStatus(meal.isInBoxStatus());
            existingMeal.get().setLabel(meal.getLabel());
            existingMeal.get().setCalories(meal.getCalories());
        }
        return ResponseEntity.ok(existingMeal.get());
    }

    public ResponseEntity<Void> deleteMealById(Long id){
        logger.warn("Deleting meal by id: {}", id);
        mealRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //Not Recommended
    public ResponseEntity<Void> deleteAllMeals(){
        logger.warn("Deleting all meals");
        mealRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteAllMealsByUserId(Long userId){
        MealBoxEntity mealBox = mealBoxRepository.findByUserId(userId).orElse(null);

        if (mealBox == null) {
            logger.error("Meal Box not found for user: {}", userId);
            throw new RuntimeException("MealBox not found");
        }

        List<MealEntity> meals = mealBox.getMeals();

        logger.info("Deleting Meals with user id: {} and meal size {}", userId, meals.size());

        //First(mealRepo.deleteById(meal.getId())) we tried deletion with deleteById but hibernate working logic thought this guy trying to
        // delete meals but we have still a relation between mealBox so I won't delete it here
        //that's why we did deletion through meal box

        meals.clear();

        mealBoxRepository.save(mealBox);

        return ResponseEntity.noContent().build();
    }

}
