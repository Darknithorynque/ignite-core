package com.example.ignite_core.WaterIntake;

import com.example.ignite_core.User.UserEntity;
import com.example.ignite_core.User.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DailyResetService {

    private final WaterIntakeRepository waterIntakeRepository;
    private final UserRepository userRepository;

    public DailyResetService(WaterIntakeRepository waterIntakeRepository, UserRepository userRepository) {
        this.waterIntakeRepository = waterIntakeRepository;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 0 0 * * *") // Runs at midnight
    public void createNewDayRecords() {
        System.out.println("Checking for missing daily water intake records...");

        List<UserEntity> allUsers = userRepository.findAll();

        LocalDate today = LocalDate.now();
        LocalDateTime now = LocalDateTime.now();

        for (UserEntity user : allUsers ) {
            boolean exists = waterIntakeRepository.findWaterIntakeByDate(user.getId(), today).isPresent();

            if (!exists) {
                WaterIntakeEntity newRecord = new WaterIntakeEntity(user.getId(), today, 0.0, now);
                waterIntakeRepository.save(newRecord);
                System.out.println("Created new water intake record for user with id" + user.getId());
            }
        }
    }
}
