package com.chex.modules.post.model;

import com.chex.modules.places.model.PlaceShortView;
import java.util.ArrayList;
import java.util.List;


public class PostView {

    private Long id;
    private Long authorId;
    private boolean isAuthor;
    private String authorName;
    private String authorPhoto;
    private String description;
    private String createdAt;
    private int stanum;
    private boolean voted;
    private List<PlaceShortView> places = new ArrayList<>();
    private List<PlaceShortView> subPlaces = new ArrayList<>();
    private List<CommentView> commentViews = new ArrayList<>();
    private List<PostPhoto> photos = new ArrayList<>();

    public PostView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorPhoto() {
        return authorPhoto;
    }

    public void setAuthorPhoto(String authorPhoto) {
        this.authorPhoto = authorPhoto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getStanum() {
        return stanum;
    }

    public void setStanum(int stanum) {
        this.stanum = stanum;
    }

    public boolean isVoted() {
        return voted;
    }

    public void setVoted(boolean voted) {
        this.voted = voted;
    }

    public List<PlaceShortView> getPlaces() {
        return places;
    }

    public void setPlaces(List<PlaceShortView> places) {
        this.places = places;
    }

    public List<PlaceShortView> getSubPlaces() {
        return subPlaces;
    }

    public void setSubPlaces(List<PlaceShortView> subPlaces) {
        this.subPlaces = subPlaces;
    }

    public List<CommentView> getCommentViews() {
        return commentViews;
    }

    public void setCommentViews(List<CommentView> commentViews) {
        this.commentViews = commentViews;
    }

    public List<PostPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PostPhoto> photos) {
        this.photos = photos;
    }
}
