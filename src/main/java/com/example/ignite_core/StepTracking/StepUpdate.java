package com.example.ignite_core.StepTracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StepUpdate {
    private Long userId;
    private int stepCount;
    private LocalDate date;

    public StepUpdate(Long userId, int stepCount) {
        this.userId = userId;
        this.stepCount = stepCount;
    }
}
