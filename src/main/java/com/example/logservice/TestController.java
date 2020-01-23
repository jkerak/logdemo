package com.example.logservice;

import com.example.logservice.logger.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    private LoggerFactory loggerFactory;

    public TestController(LoggerFactory factory) {
        this.loggerFactory = factory;
    }


    @GetMapping("/info/{loggerName}")
    public String LogInfo(@PathVariable String loggerName){

        var message = "logging some INFO from " + loggerName;
        var logger = loggerFactory.get(loggerName);
        logger.logInfo(message);
        return "INFO logged";
    }

    @GetMapping("/error/{loggerName}")
    public String LogError(@PathVariable String loggerName){

        var message = "logging some ERROR from " + loggerName;

        var logger = loggerFactory.get(loggerName);
        logger.logError(message);
        return "ERROR logged";
    }

    @GetMapping("/debug/{loggerName}")
    public String LogDebug(@PathVariable String loggerName){

        var message = "logging some DEBUG from " + loggerName;

        var logger = loggerFactory.get(loggerName);
        logger.logDebug(message);
        return "DEBUG logged";
    }


}
