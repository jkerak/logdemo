package com.example.logservice.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table
public class LogEntryEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @ToString.Exclude
    private int id;

    @ToString.Exclude
    private int batchId;
    private String message, logLevel;
}
