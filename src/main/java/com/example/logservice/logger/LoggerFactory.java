package com.example.logservice.logger;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoggerFactory {

    private final Map<String, Logger> loggerBeans;

    public LoggerFactory(Map<String, Logger> loggerBeans) {
        for (var key: loggerBeans.keySet()) {
            loggerBeans.get(key).Initialize();
        }
        this.loggerBeans = loggerBeans;
    }

    public Logger get(String impl) {
        return this.loggerBeans.get(impl);
    }
}
