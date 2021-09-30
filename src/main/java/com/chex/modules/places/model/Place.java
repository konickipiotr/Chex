package com.chex.modules.places.model;

import com.chex.webapp.admin.places.newplace.PlaceForm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Place {

    /*
    id - Field of 16 characters
    1-2 - continent
    3-5 - country
    6-8 - region
    9-11 - city/province
    12-16 - specific point
     */
    @Id
    @Column(length = 20)
    private String id;
    private double longitude;
    private double latitude;
    private double radius;
    private Long category;
    private String achievements;
    private int points;
    private int difficultylevel;
    private double rating;
    private double votesnum;
    private String imgurl;
    private String imgpath;

    public Place() {
        this.latitude = 10000;
        this.longitude = 10000;
        this.radius = 0;
    }

    public Place(PlaceForm placeForm){
        this();

        this.id = placeForm.createId();
        this.points = placeForm.getPoints();
        this.difficultylevel = 1;
        if(placeForm.getPrefix().length() > 11) {
            this.longitude = placeForm.getLongitude();
            this.latitude = placeForm.getLatitude();
            this.category = placeForm.getCategory();
            this.radius = placeForm.getRadius();
            this.difficultylevel = placeForm.getDifficultylevel();
        }
    }

    public Place(String id, Long category, int points, String imgurl) {
        this.id = id;
        this.category = category;
        this.points = points;
        this.imgurl = imgurl;
    }

    public Place(String id) {
        this.id = id;
    }

    public void addVote(int grade){
        this.votesnum++;
        this.rating = (this.rating + grade) / this.votesnum;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDifficultylevel() {
        return difficultylevel;
    }

    public void setDifficultylevel(int difficultylevel) {
        this.difficultylevel = difficultylevel;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getVotesnum() {
        return votesnum;
    }

    public void setVotesnum(double votesnum) {
        this.votesnum = votesnum;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
}
