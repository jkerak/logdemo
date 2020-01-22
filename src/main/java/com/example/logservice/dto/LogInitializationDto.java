package com.example.logservice.dto;

import lombok.Data;

@Data
public class LogInitializationDto {
    private String loggerName;
    private int interval;
    private String host;
    private int port;
    private String userName;
    private String password;
    private String LogFileName;
    private String level;
}
