package com.example.logservice.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "LogEntry")
public class LogEntryEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private int batchId;
    private String message, logLevel;
}
