package com.photosaver.photosaver;

import com.photosaver.photosaver.scheduler.CronJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    CronJobService cronJobService;

    @Override
    public void run(String... args) {
        cronJobService.agileengineComCronJob();
    }
}
