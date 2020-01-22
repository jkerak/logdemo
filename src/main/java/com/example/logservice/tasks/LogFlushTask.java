package com.example.logservice.tasks;

import com.example.logservice.dto.LogInitializationDto;
import com.example.logservice.repository.LogBatchRepository;
import com.example.logservice.repository.LogRepository;
import com.gc.iotools.stream.os.OutputStreamToInputStream;
import lombok.SneakyThrows;
import org.apache.commons.net.ftp.FTPClient;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class LogFlushTask implements Runnable {

    private LogBatchRepository logBatchRepository;
    private LogRepository logRepository;
    private LogInitializationDto logInitializationDto;

    public LogFlushTask(LogBatchRepository logBatchRepository, LogRepository logRepository, LogInitializationDto logInitializationDto) {
        this.logBatchRepository = logBatchRepository;
        this.logRepository = logRepository;
        this.logInitializationDto = logInitializationDto;
    }

    // tossing @SneakyThrows on here because this isn't a real production app
    @SneakyThrows
    @Override
    public void run() {

        // Get the current batch ID for this logger
        var currentBatch = logBatchRepository.findByLoggerName(logInitializationDto.getLoggerName());

        // Increment this logger's batch ID
        logBatchRepository.incrementLoggerBatch(currentBatch.getLoggerName());

        // Get all the log entries for the now-previous batch
        var entries = logRepository.findAllByBatchId(currentBatch.getBatchId());

        // do nothing if there are no log entries to record
        if(entries.size() == 0){
            return;
        }

        // create FTP connection
        var ftp = new FTPClient();
        ftp.connect(logInitializationDto.getHost(),logInitializationDto.getPort());
        ftp.login(logInitializationDto.getUserName(), logInitializationDto.getPassword());


        // using easystream to get an InputStream from the OutputStream we are inserting row by row
        try (final var os2is = new OutputStreamToInputStream<Void>() {
            @Override
            protected Void doRead(final InputStream inputStream) throws Exception {
                String ts = DateTimeFormatter.ofPattern("_yyyyMMdd_HHmmss").format(LocalDateTime.now());
                ftp.storeFile(logInitializationDto.getLogFileName() + ts + ".txt",inputStream);
                return null;
            }
        }) {

            for (var entry : entries) {
                os2is.write(entry.toString().getBytes());
                os2is.write("\n".getBytes());
            }
        }

        System.out.println("log entries: " + entries.size());
    }
}
