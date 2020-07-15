package com.photosaver.photosaver.dao;

import com.photosaver.photosaver.entity.Image;
import com.photosaver.photosaver.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="pictures")
public interface PictureRepository extends JpaRepository<Picture, String> {
}
