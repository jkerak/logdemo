package com.example.logservice.repository;

import com.example.logservice.entity.LogEntryEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface LogRepository extends CrudRepository<LogEntryEntity, Integer> {

    public List<LogEntryEntity> findAllByBatchId(int batchId);
}