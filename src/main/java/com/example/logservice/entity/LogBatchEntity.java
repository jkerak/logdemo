package com.example.logservice.entity;

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
