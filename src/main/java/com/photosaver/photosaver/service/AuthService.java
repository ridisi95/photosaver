package com.photosaver.photosaver.service;

import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final String URL_FOR_NEW_TOKEN = "http://interview.agileengine.com/auth";
    private static final String BODY_KEY = "apiKey";
    private static final String BODY_VALUE = "23567b218376f79d9415";
    private static final String TOKEN = "token";

    @Cacheable(TOKEN)
    public HttpHeaders getHeaderWithToken() {
        return createHeaderWithNewToken();
    }

    @CachePut(TOKEN)
    public HttpHeaders updateHeaderWithActualToken() {
        HttpHeaders httpHeaders = createHeaderWithNewToken();
        logger.info("token updated");
        return httpHeaders;
    }

    private HttpHeaders createHeaderWithNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        JSONObject request = new JSONObject();
        request.put(BODY_KEY, BODY_VALUE);

        logger.info("do request for a new token");
        String token = restTemplate.postForEntity(URL_FOR_NEW_TOKEN, request, JSONObject.class)
                .getBody().getAsString(TOKEN);
        logger.info("received a new token: " + token);

        return new HttpHeaders() {{
            set("Authorization", "Bearer " + token);
        }};
    }
}
