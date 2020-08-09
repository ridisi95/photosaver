package com.photosaver.photosaver.dao;

import com.photosaver.photosaver.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path="pictures")
public interface PictureRepository extends JpaRepository<Picture, String> {

    public List<Picture> findAllByCamera(@Param("camera") String camera);
}
