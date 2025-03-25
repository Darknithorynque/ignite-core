package com.example.ignite_core.StepTracking;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/step-tracking")
public class StepTrackingController {
    private final StepTrackingService service;
    private final StepTrackingRepository repository;

    public StepTrackingController(StepTrackingService service, StepTrackingRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping("/update")
    public ResponseEntity<StepTrackingEntity> updateStep(@RequestParam Long userId, @RequestParam int steps, @RequestParam LocalDate date) {
       return service.updateRecord(userId, steps,date);
    }

    @GetMapping("/{userId}")
    public StepUpdate getCurrentSteps(@PathVariable Long userId) {
        return new StepUpdate(userId, service.getSteps(userId));
    }

    @GetMapping("/{userId}/{date}")
    public StepUpdate getStepsByDate(@PathVariable Long userId, @PathVariable LocalDate date) {

        Optional<StepTrackingEntity> record = repository.findByUserIdAndDate(userId, date);
        return record.map(r -> new StepUpdate(userId, r.getStepCount()))
                .orElse(new StepUpdate(userId, 0));
    }
}
