package com.example.ignite_core.WeightReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeightLogRepository extends JpaRepository<WeightLogEntity, Long> {

    @Query("SELECT w FROM WeightLogEntity w WHERE w.user.id = :userId and w.reportedAt BETWEEN :from AND :to")
    List<WeightLogEntity> queryWeightLogByUserIdBetween(@Param("userId") Long userId,@Param("from") LocalDate from,@Param("to") LocalDate to);

    @Query("SELECT w FROM WeightLogEntity w WHERE w.user.id = :userId")
    List<WeightLogEntity> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM WeightLogEntity w WHERE w.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

}
