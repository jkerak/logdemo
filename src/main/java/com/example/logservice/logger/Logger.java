package com.example.logservice.logger;

import com.example.logservice.model.LogEntryDto;
import com.example.logservice.model.LogInitializationDto;
import com.example.logservice.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;

public class Logger {

    private LogInitializationDto initializationDto;

    @Autowired
    private LogService logService;


    public Logger(LogInitializationDto initializationDto) {
        this.initializationDto = initializationDto;
    }

    // we always log error
    public void logError(String message) {
        logService.LogMessage(new LogEntryDto(message, initializationDto.getLoggerName(), "ERROR"));
    }

    // only log INFO if configured for INFO or DEBUG
    public void logInfo(String message) {
        if (initializationDto.getLevel().equals("INFO") || initializationDto.getLevel().equals("DEBUG")) {
            logService.LogMessage(new LogEntryDto(message, initializationDto.getLoggerName(), "INFO"));
        }
    }

    // only log DEBUG if configured for DEBUG
    public void logDebug(String message) {
        if (initializationDto.getLevel().equals("DEBUG")) {
            logService.LogMessage(new LogEntryDto(message, initializationDto.getLoggerName(), "DEBUG"));
        }
    }

    public void Initialize(){
        logService.InitializeLogger(initializationDto);

    }
}
