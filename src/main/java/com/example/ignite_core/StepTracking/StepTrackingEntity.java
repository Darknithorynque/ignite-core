package com.example.ignite_core.StepTracking;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "step_records")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StepTrackingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private int stepCount;
    private LocalDate date;
    private LocalDateTime lastUpdate;

    public StepTrackingEntity(Long userId, Integer step, LocalDate date) {
        this.userId = userId;
        this.stepCount = step;
        this.date = date;
    }

}
