package com.example.logservice.service;

import com.example.logservice.dto.LogEntryDto;
import com.example.logservice.dto.LogInitializationDto;
import com.example.logservice.entity.LogBatchEntity;
import com.example.logservice.entity.LogEntryEntity;
import com.example.logservice.repository.LogBatchRepository;
import com.example.logservice.repository.LogRepository;
import com.example.logservice.tasks.LogFlushTask;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class LogService {

    private final LogBatchRepository logBatchRepository;
    private final LogRepository logRepository;
    private final ThreadPoolTaskScheduler taskScheduler;

    public LogService(LogBatchRepository logBatchRepository, LogRepository logRepository, ThreadPoolTaskScheduler taskScheduler) {
        this.logBatchRepository = logBatchRepository;
        this.logRepository = logRepository;
        this.taskScheduler = taskScheduler;
    }

    public void LogMessage(LogEntryDto logEntry){

        // get the current batch ID
        var currentBatch = logBatchRepository.findByLoggerName(logEntry.getLoggerName());

        // build the log entry entity
        var entity = new LogEntryEntity();
        entity.setMessage(logEntry.getMessage());
        entity.setBatchId(currentBatch.getBatchId());

        // insert log entry to db
        logRepository.save(entity);
    }


    public void InitializeLogger(LogInitializationDto dto){

        // initialize batch with ID 0
        var batchEntity = new LogBatchEntity();
        batchEntity.setBatchId(0);
        batchEntity.setLoggerName(dto.getLoggerName());
        logBatchRepository.saveAndFlush(batchEntity);
        var task = new LogFlushTask(logBatchRepository, logRepository, dto);

        // schedule to run on background thread on interval
        taskScheduler.scheduleAtFixedRate(task, dto.getInterval()*1000*60);
    }
}
