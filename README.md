**Sample Logging Project using java/spring boot**

Requirements:
- Java 11
- maven

To run:

`mvn spring-boot:run`

This will boot and and start the logging project/application via an embedded tomcat container running on http://localhost:8080/. It is a sample application that defines a "logging service" that can be injected into any spring boot application. For the purposes of the demo, I included a sample rest controller (`TestController`) as an easy way to execute some test log entries via the browser.
 

From a browser, you can visit `http://localhost:8080/{logLevel}/{loggerName}` to test inserting log entries. The REST controller would not be required to use this logging service, it was just an easy way for me to test using a framework I am familiar with. 
   
   
   For example, assuming the following logger configuration as defined in `loggerConfig.yml`:
```
logger1:
    interval: 1 # minutes
    host: couchdrop.io
    port: 21
    username: jim
    password: test
    log-file-name: logger1.log
    level: INFO
logger2:
    interval: 2 # minutes
    host: couchdrop.io
    port: 21
    username: jim
    password: test
    log-file-name: logger2.log
    level: INFO
```

You could visit http://localhost:8080/info/logger1 to send an INFO-level canned log message to logger1.


For the purposes of this exercise to test ende to end, I set up a simple public FTP site using couchdrop.io that is backed by an S3 bucket.

Therefore, if the project is cloned and run exactly as configured here, you will find log files appearing here:

https://jk-logger.s3.us-east-2.amazonaws.com/ (list-objects is allowed publicly for sandbox purposes)



A few things to note:

I decided against using local file storage, and rather used an in-memory database (H2) to store the log entries. This was to make it easier to atomically batch log entries together safely, and because the file was going to be deleted anyway. This would also allow the application to run anywhere without local file system access.

For an arbitrary developer to use this logging library, the idea is to inject a `LoggerFactory`
 anywhere it is needed, like this:
 
 ```
private LoggerFactory loggerFactory;

public TestController(LoggerFactory factory) {
    this.loggerFactory = factory;
}


@GetMapping("/info/{loggerName}/")
public String LogInfo(@PathVariable String loggerName){

    var message = "logging some INFO from " + loggerName;
    var logger = loggerFactory.get(loggerName);
    logger.logInfo(message);
    return "INFO logged";
}
```

The logger factory takes care of resolving the correct logger via the name of the logger, as well as kicking off the periodic "log flush task" which actually generates and uploads the file to the ftp site.

The periodic "roll over" is done using `ThreadPoolTaskScheduler`

The ftp upload is done using Apache Commons Net

Maven dependencies used:

- spring-boot-starter-data-jpa (for easy CRUD without writing SQL)
- spring-boot-starter-web (for the TestController)
- lombok (for faster writing of POJOs)
- h2 (in-memory db)
- snakeyaml (for reading the yaml config)
- commons-net (for FTPClient)
- easystream (to easily build the file stream for SFTP in memory)






