package com.photosaver.photosaver.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Objects;

@Entity
@Table(name = "pictures")
public class Picture {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "author")
    private String author;

    @Column(name = "camera")
    private String camera;

    @Column(name = "tags")
    private String tags;

    @Transient
    private String cropped_picture;

    @Transient
    private String full_picture;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="cropped_picture_id")
    private Image croppedPicture;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="full_picture_id")
    private Image fullPicture;

    public Picture() {
    }

    public Picture(String id) {
        this.id = id;
    }

    public Picture(String id, String author, String camera, String tags, String cropped_picture, String full_picture,
                   Image croppedPicture, Image fullPicture) {
        this.id = id;
        this.author = author;
        this.camera = camera;
        this.tags = tags;
        this.cropped_picture = cropped_picture;
        this.full_picture = full_picture;
        this.croppedPicture = croppedPicture;
        this.fullPicture = fullPicture;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCropped_picture() {
        return cropped_picture;
    }

    public void setCropped_picture(String cropped_picture) {
        this.cropped_picture = cropped_picture;
    }

    public String getFull_picture() {
        return full_picture;
    }

    public void setFull_picture(String full_picture) {
        this.full_picture = full_picture;
    }

    public Image getCroppedPicture() {
        return croppedPicture;
    }

    public void setCroppedPicture(Image croppedPictureId) {
        this.croppedPicture = croppedPictureId;
    }

    public Image getFullPicture() {
        return fullPicture;
    }

    public void setFullPicture(Image fullPictureId) {
        this.fullPicture = fullPictureId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Picture picture = (Picture) o;
        return Objects.equals(id, picture.id) &&
                Objects.equals(author, picture.author) &&
                Objects.equals(camera, picture.camera) &&
                Objects.equals(tags, picture.tags) &&
                Objects.equals(cropped_picture, picture.cropped_picture) &&
                Objects.equals(full_picture, picture.full_picture) &&
                Objects.equals(croppedPicture, picture.croppedPicture) &&
                Objects.equals(fullPicture, picture.fullPicture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author, camera, tags, cropped_picture, full_picture, croppedPicture, fullPicture);
    }
}
