package com.photosaver.photosaver.scheduler;

import com.photosaver.photosaver.service.AuthService;
import com.photosaver.photosaver.service.PictureLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CronJobService {
    private static final Logger logger = LoggerFactory.getLogger(CronJobService.class);

    @Autowired
    private PictureLoaderService loaderService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AuthService authService;


//    @Scheduled(fixedRate = 120000)
    public void agileengineComCronJob() {
        logger.info("cronJobSch has started");
        logger.info("================================================================================================");
        long timeMillis = System.currentTimeMillis();

        loaderService.loadPicturesFromAgileengineCom();

        logger.info("cronJobSch is finished for " + ((System.currentTimeMillis() - timeMillis) / 1000) + " seconds");
        logger.info("================================================================================================");
    }
}
