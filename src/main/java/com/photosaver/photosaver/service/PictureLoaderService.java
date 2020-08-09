package com.photosaver.photosaver.service;

import com.photosaver.photosaver.dao.PictureRepository;
import com.photosaver.photosaver.entity.Image;
import com.photosaver.photosaver.entity.ImageJson;
import com.photosaver.photosaver.entity.Picture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class PictureLoaderService {

    private static final Logger logger = LoggerFactory.getLogger(PictureLoaderService.class);

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private RestTemplate restTemplate;

    public void loadPicturesFromAgileengineCom() {
        ResponseEntity<ImageJson> response;
        AtomicInteger pageNumber = new AtomicInteger(0);

        ExecutorService executorService = Executors.newFixedThreadPool(7);
        do {
            String url = "http://interview.agileengine.com/images?page=" + pageNumber.incrementAndGet() ;
            logger.info("page number is " + pageNumber);

            response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(authService.getHeaderWithToken()),
                    ImageJson.class);

            List<String> picturesId = response.getBody().getPictures().stream()
                    .map(Picture::getId).collect(Collectors.toList());

            executorService.execute(() -> updatePictureInfo(picturesId));
        } while (response.getBody().isHasMore());
        executorService.shutdown();

        while(!executorService.isTerminated());
    }

    private void updatePictureInfo(List<String> picturesId) {
        logger.info("current thread " + Thread.currentThread().getName());
        List<Picture> alreadyStoredPictures = Optional.of(pictureRepository.findAllById(picturesId))
                .orElse(new ArrayList());

        List<Picture> shouldBeSavedPictures = new ArrayList(picturesId.size());

        for (String pictureId : picturesId) {
            String url = "http://interview.agileengine.com/images/" + pictureId;
            logger.info("picture with id = " + pictureId + " is processed");

            ResponseEntity<Picture> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(authService.getHeaderWithToken()),
                    Picture.class);

            Picture storedPicture = alreadyStoredPictures.stream()
                    .filter(picture -> picture.getId().equals(pictureId)).findFirst().orElse(null);
            Picture receivedPicture = response.getBody();

            if (storedPicture == null || !isPicturesEqual(receivedPicture, storedPicture)) {
                addOrUpdatePicture(receivedPicture);
                shouldBeSavedPictures.add(receivedPicture);
                logger.info("picture with id = " + pictureId + " has been saved");
            }
        }

        pictureRepository.saveAll(shouldBeSavedPictures);
    }

    private void addOrUpdatePicture(Picture receivedPicture) {
        String croppedImageLink = receivedPicture.getCropped_picture();
        Image croppedPicture = new Image(croppedImageLink, downloadPicture(croppedImageLink));
        receivedPicture.setCroppedPicture(croppedPicture);

        String fullImageLink = receivedPicture.getFull_picture();
        Image fullPicture = new Image(fullImageLink, downloadPicture(fullImageLink));
        receivedPicture.setFullPicture(fullPicture);
    }

    private boolean isPicturesEqual(Picture receivedPicture, Picture storedPicture) {
        return Objects.equals(receivedPicture.getAuthor(), storedPicture.getAuthor())
                && Objects.equals(receivedPicture.getCamera(), storedPicture.getCamera())
                && Objects.equals(receivedPicture.getTags(), storedPicture.getTags())
                && Objects.equals(receivedPicture.getCropped_picture(), storedPicture.getCroppedPicture().getImageLink())
                && Objects.equals(receivedPicture.getFull_picture(), storedPicture.getFullPicture().getImageLink());
    }

    private byte[] downloadPicture(String imageLink) {
        logger.info("current thread " + Thread.currentThread().getName());

        ResponseEntity<byte[]> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
            response = restTemplate.exchange(imageLink, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);
            logger.info("Image downloaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.getBody();
    }
}
