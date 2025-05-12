package com.example.ignite_core.WeightReport;

import com.example.ignite_core.User.Model.UserEntity;
import com.example.ignite_core.User.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class WeightLogService {

    private final WeightLogRepository weightLogRepository;
    private final UserRepository userRepository;

    private final Logger log = LoggerFactory.getLogger(WeightLogService.class);

    public WeightLogService(WeightLogRepository weightLogRepository, UserRepository userRepository) {
        this.weightLogRepository = weightLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<WeightLogEntity> logAndUpdateWeight(double newWeight, Long userId) {
        log.debug("logAndUpdateWeight process starting..");
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("logAndUpdateWeight process failed for userId not found: {}", userId);
           return new RuntimeException("User not found");
        });

        double previousWeight = user.getWeight();
        log.debug("previousWeight: {}", previousWeight);

        user.setWeight(newWeight);
        userRepository.save(user);

        WeightLogEntity weightLog = new WeightLogEntity();

        weightLog.setUser(user);
        weightLog.setNewWeight(newWeight);
        weightLog.setPreviousWeight(previousWeight);
        weightLogRepository.save(weightLog);
        log.info("logAndUpdateWeight done for userId, new weight: {} {}", userId, newWeight);

        return ResponseEntity.ok(weightLog);

    }

    public ResponseEntity<List<WeightLogEntity>> getAllWeightLogs() {
       try{
           log.debug("getAllWeightLogs process starting..");
           List<WeightLogEntity> allWeightLogs = weightLogRepository.findAll();
           log.info("Successfully fetched weight log records");

           return ResponseEntity.ok(allWeightLogs);
       } catch (DataAccessException e) {
           log.error("Error occurred while fetching all weight logs");
           return ResponseEntity.internalServerError().body(null);
       }

    }

    public ResponseEntity<List<WeightLogEntity>> getWeightLogByUserId(Long userId) {
        log.debug("getWeightLogByUserId process starting for userId: {}", userId);

        try {
            List<WeightLogEntity> weightLog = weightLogRepository.findByUserId(userId);

            if (!weightLog.isEmpty()) {
                log.info("Successfully fetched weight log record for user with id: {}", userId);
                return ResponseEntity.ok(weightLog);
            } else {
                log.warn("No record found for user with id: {}", userId);
                return ResponseEntity.noContent().build();
            }
        } catch (DataAccessException e){
            log.error("Error occurred while fetching weight logs for userId: {}", userId);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    public ResponseEntity<WeightLogEntity> getWeightLogById(Long id) {
        log.debug("getWeightLogById process starting for id: {}", id);

        try{
            Optional<WeightLogEntity> weightLog = weightLogRepository.findById(id);

            if (weightLog.isPresent()) {
                log.info("Successfully fetched weight log record for id: {}", id);
                return ResponseEntity.ok(weightLog.get());
            } else {
                log.warn("Weight log record not found for id: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (DataAccessException e) {
            log.error("Error occurred while fetching weight log for id: {}", id);
            return ResponseEntity.internalServerError().body(null);
        }

    }

    public ResponseEntity<List<WeightLogEntity>> queryWeightLogsByUserIdBetween(Long userId, LocalDate from, LocalDate to) {
        try {
            log.debug("queryWeightLogsByUserIdBetween process starting for userId: {}, from: {}, to: {}", userId, from, to);
            List<WeightLogEntity> weightLogRecordsBetween = weightLogRepository.queryWeightLogByUserIdBetween(userId, from, to);

            if (userRepository.existsById(userId)) {
                log.info("Successfully fetched weight log date between user with userId: {}, from: {}, to: {}", userId, from, to);
                return ResponseEntity.ok(weightLogRecordsBetween);
            } else {
                log.warn("Weight log record not found for userId: {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(weightLogRecordsBetween);
            }
        } catch (DataAccessException e) {
            log.error("Error occurred while querying weight logs between user with userId: {}", userId);
            return ResponseEntity.internalServerError().body(null);
        }
    }

    public ResponseEntity<String> deleteWeightLogById(Long id) {
        log.debug("Attempting to delete weight log record for id: {}", id);

        if (!weightLogRepository.existsById(id)) {
            log.error("Weight log record not found for id: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Weight log record not found for id: " + id);
        }

        try {
            weightLogRepository.deleteById(id);
            log.info("Successfully deleted weight log for id: {}", id);

            return ResponseEntity.ok("Weight log record deleted successfully");
        } catch (DataAccessException e) {
            log.error("Error occurred while deleting weight log for id: {}", id);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<String> deleteWeightLogsByUserId(Long userId) {
        log.debug("Attempting to delete weight log records for userId: {}", userId);
        if(!userRepository.existsById(userId)) {
            log.error("User record not found for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User record not found for userId: " + userId);
        }
        try {
            weightLogRepository.deleteByUserId(userId);
            log.info("Successfully deleted weight logs for userId: {}", userId);

            return ResponseEntity.ok("Weight log record deleted successfully");
        } catch (DataAccessException e) {
            log.error("Error occurred while deleting weight logs for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
