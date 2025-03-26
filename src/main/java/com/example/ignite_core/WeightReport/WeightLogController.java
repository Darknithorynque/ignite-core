package com.example.ignite_core.WeightReport;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/weightLog")
public class WeightLogController {

    private final WeightLogService weightLogService;

    public WeightLogController(WeightLogService weightLogService) {
        this.weightLogService = weightLogService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<WeightLogEntity>> getAllWeightLogs() {
        return weightLogService.getAllWeightLogs();
    }

    @GetMapping("/between/{userId}")
    @JsonView(Views.Basic.class)
    public ResponseEntity<List<WeightLogEntity>> queryWeightLogByUserIdBetween(@PathVariable Long userId, @RequestParam LocalDate from,@RequestParam LocalDate to) {
        return weightLogService.queryWeightLogsByUserIdBetween(userId,from,to);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeightLogEntity> getWeightLogById(@PathVariable Long id) {
        return weightLogService.getWeightLogById(id);
    }

    @GetMapping("/byUserId/{userId}")
    @JsonView(Views.Basic.class)
    public ResponseEntity<List<WeightLogEntity>> getWeightLogsByUserId(@PathVariable Long userId){
        return weightLogService.getWeightLogByUserId(userId);
    }

    @PostMapping("/log/{userId}")
    public ResponseEntity<WeightLogEntity> logAndUpdateWeightLog(@RequestParam double newWeight,@PathVariable long userId) {
        return weightLogService.logAndUpdateWeight(newWeight,userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWeightLogById(@PathVariable Long id) {
        return weightLogService.deleteWeightLogById(id);
    }

    @DeleteMapping("/byUserId/{userId}")
    public ResponseEntity<String> deleteWeightLogByUserId(@PathVariable Long userId) {
        return weightLogService.deleteWeightLogsByUserId(userId);
    }
}
