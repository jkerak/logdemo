package com.example.logservice;

import com.example.logservice.dto.LogInitializationDto;
import com.example.logservice.logger.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.logservice.repository")
@EnableScheduling
public class LogServiceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new SpringApplicationBuilder()
				.sources(LogServiceApplication.class)
				.initializers((ApplicationContextInitializer<GenericWebApplicationContext>) ctx -> {

					// load the yaml config from resources
					var yaml = new Yaml();
					InputStream inputStream = LogServiceApplication.class
							.getClassLoader()
							.getResourceAsStream("loggerConfig.yml");
					Map<String, LinkedHashMap<String, Object>> obj = yaml.load(inputStream);


					// for each configured logger, register a named bean.
					for (String key : obj.keySet()) {
						var item = obj.get(key);
						LogInitializationDto initializationDto = getLogInitializationDto(key, item);
						ctx.registerBean(key, Logger.class, () -> new Logger(initializationDto));
					}
				})
				.run(args);
	}


	// just populating a pojo containing config settings from yaml
	private static LogInitializationDto getLogInitializationDto(String key, LinkedHashMap<String, Object> item) {
		var initializationDto = new LogInitializationDto();

		initializationDto.setLevel(item.get("level").toString());
		initializationDto.setInterval((int) item.get("interval"));
		initializationDto.setHost(item.get("host").toString());
		initializationDto.setPort((int)item.get("port"));
		initializationDto.setUserName(item.get("username").toString());
		initializationDto.setPassword(item.get("password").toString());
		initializationDto.setLogFileName(item.get("log-file-name").toString());

		initializationDto.setLoggerName(key);
		return initializationDto;
	}


	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler(){
		ThreadPoolTaskScheduler threadPoolTaskScheduler
				= new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(10);
		threadPoolTaskScheduler.setThreadNamePrefix(
				"ThreadPoolTaskScheduler");
		return threadPoolTaskScheduler;
	}
}
