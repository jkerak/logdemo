package com.example.logservice.repository;

import com.example.logservice.entity.LogBatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface LogBatchRepository extends JpaRepository<LogBatchEntity, String> {
    LogBatchEntity findByLoggerName(String loggerName);

    @Transactional
    @Modifying
    @Query("UPDATE LogBatchEntity SET batchId = batchId + 1 where loggerName = :loggerName")
    void incrementLoggerBatch(String loggerName);
}
