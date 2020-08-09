package com.photosaver.photosaver.aop;

import com.photosaver.photosaver.service.AuthService;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Aspect
@Component
public class ExceptionCatcherAspect {

    @Autowired
    private AuthService authService;

    private static final Logger logger = LoggerFactory.getLogger(ExceptionCatcherAspect.class);

    @AfterThrowing(
            pointcut = "execution(public void com.photosaver.photosaver.scheduler.CronJobService.agileengineComCronJob())",
            throwing = "exc")
    public void unauthorizedUserExceptionAdvice(Throwable exc) {

        if (exc instanceof HttpClientErrorException.Unauthorized) {
            logger.warn("401 Unauthorized user");
            authService.updateHeaderWithActualToken();
        }
    }
}
