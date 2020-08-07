package com.photosaver.photosaver.scheduler;

import com.photosaver.photosaver.service.PictureLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    @Autowired
    private PictureLoaderService loaderService;

    @Scheduled(fixedRate = 100000)
    public void cronJobSch() {
        logger.info("cronJobSch has started");
        logger.info("================================================================================================");
        long timeMillis = System.currentTimeMillis();

        loaderService.loadPicturesFromAgileengineCom();

        logger.info("cronJobSch is finished for " + ((System.currentTimeMillis() - timeMillis) / 1000) + " seconds");
        logger.info("================================================================================================");
    }
}
