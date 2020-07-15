package com.photosaver.photosaver.scheduler;

import com.photosaver.photosaver.dao.PictureRepository;
import com.photosaver.photosaver.entity.Image;
import com.photosaver.photosaver.entity.ImageJson;
import com.photosaver.photosaver.entity.Picture;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class Scheduler {

    private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

    @Autowired
    private PictureRepository pictureRepository;

    @Scheduled(fixedRate = 120000)
    public void cronJobSch() {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<ImageJson> response = null;
        int pageNumber = 0;
        HttpHeaders freshToken = getRefreshToken(restTemplate);
        do {
            String url = "http://interview.agileengine.com/images?page=" + ++pageNumber;

            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(freshToken),
                    ImageJson.class);

            updatePictureInfo(response.getBody().getPictures(), restTemplate, freshToken);

        } while (response != null && response.getBody().isHasMore());

        logger.info("cronJobSch is finished");
    }

    private HttpHeaders getRefreshToken(RestTemplate restTemplate) {
        String url = "http://interview.agileengine.com/auth";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        JSONObject request = new JSONObject();
        request.put("apiKey", "23567b218376f79d9415");

        String token = restTemplate.postForEntity( url, request , JSONObject.class).getBody().getAsString("token");

        return new HttpHeaders() {{
            set("Authorization", "Bearer " + token);
        }};
    }

    private void updatePictureInfo(List<Picture> pictures, RestTemplate restTemplate, HttpHeaders freshToken) {
        for(Picture picture: pictures) {

            String url = "http://interview.agileengine.com/images/" + picture.getId();

            ResponseEntity<Picture> response1 = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(freshToken),
                    Picture.class);


            Picture receivedPicture = response1.getBody();

//            picture.setCropped_picture(receivedPicture.getCropped_picture());
            picture.setFullPicture(receivedPicture.getFullPicture());
            picture.setAuthor(receivedPicture.getAuthor());
            picture.setCamera(receivedPicture.getCamera());
            picture.setTags(receivedPicture.getTags());


            String croppedImageLink = receivedPicture.getCropped_picture();
            Image croppedPicture = new Image(croppedImageLink, downloadPicture(croppedImageLink, restTemplate));
            picture.setCroppedPicture(croppedPicture);

            String fullImageLink = receivedPicture.getFull_picture();
            Image fullPicture = new Image(fullImageLink, downloadPicture(fullImageLink, restTemplate));
//            picture.setFullPicture(fullPicture);

            logger.info("picture id = " + picture.getId());
            logger.info("");
        }

        pictureRepository.saveAll(pictures);
        logger.info("All objects saved");
    }

    private byte[] downloadPicture(String imageLink, RestTemplate restTemplate) {
        ResponseEntity<byte[]> response = null;
        try {
            HttpHeaders headers1 = new HttpHeaders();
            headers1.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
            HttpEntity<String> entity = new HttpEntity<>(headers1);
            response = restTemplate.exchange(imageLink, HttpMethod.GET, entity, byte[].class);
            logger.info("File downloaded successfully ");
        }catch (Exception e){
            e.printStackTrace();
        }

        return response.getBody();
    }
}
