package com.example.logservice.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "LogBatch")
@Data
public class LogBatchEntity
{
    @Id
    private String loggerName;

    private int batchId;
}
