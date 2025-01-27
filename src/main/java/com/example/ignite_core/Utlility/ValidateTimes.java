package com.example.ignite_core.Utlility;

import java.time.LocalDateTime;

public class ValidateTimes {
    public static void validateTimes(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start Date must be after End Date");
        }
    }
}
