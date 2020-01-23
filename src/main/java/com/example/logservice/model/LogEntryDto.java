package com.example.logservice.model;

import lombok.Data;

@Data
public class LogEntryDto {
    private String message;
    private String loggerName;
    private String logLevel;

    public LogEntryDto(String message, String loggerName, String logLevel) {
        this.message = message;
        this.loggerName = loggerName;
        this.logLevel = logLevel;
    }
}
