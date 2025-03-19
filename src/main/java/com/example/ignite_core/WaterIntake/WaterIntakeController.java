package com.example.ignite_core.WaterIntake;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/api/waterIntake")
public class WaterIntakeController {

    private final WaterIntakeService service;
    public WaterIntakeController(WaterIntakeService service) {
        this.service = service;
    }
    
    @PostMapping("/log/{userId}")
    public ResponseEntity<WaterIntakeEntity> logWaterIntake(
            @PathVariable Long userId,
            @RequestParam double amount) {
        return service.logWaterIntake(userId, amount);
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<Optional<WaterIntakeEntity>> getWaterIntakeByDateAndUserId(
            @PathVariable Long userId,
            @RequestParam LocalDate date) {
        return service.findWaterIntakeByDate(userId, date);
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<WaterIntakeEntity> updateWaterIntake(
            @PathVariable Long userId,
            @RequestParam double amount,
            @RequestParam LocalDate date) {
        return service.updateWaterIntake(userId, amount, date);
    }
    
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteWaterIntakeByUserId(
            @PathVariable Long userId,
            @RequestParam LocalDate date) {
        return service.deleteWaterIntake(userId, date);
    }

}
