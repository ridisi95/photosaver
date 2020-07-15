package com.photosaver.photosaver.dao;

import com.photosaver.photosaver.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="images")
public interface ImageRepository extends JpaRepository<Image, Integer> {
}
