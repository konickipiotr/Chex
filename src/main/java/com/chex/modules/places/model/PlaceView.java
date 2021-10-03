package com.chex.modules.places.model;

import com.chex.api.profle.CompleteStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceView {

    private String id;
    private String name;
    private String description;
    private String img;
    private String svg;
    private int points;
    private int difficultLvl;
    private long usersReached;
    private int userRating;
    private double placeRating;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime achievedAt;
    private boolean subplace;
    private CompleteStatus completeStatus;

    public PlaceView() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDifficultLvl() {
        return difficultLvl;
    }

    public void setDifficultLvl(int difficultLvl) {
        this.difficultLvl = difficultLvl;
    }

    public long getUsersReached() {
        return usersReached;
    }

    public void setUsersReached(long usersReached) {
        this.usersReached = usersReached;
    }

    public int getUserRating() {
        return userRating;
    }

    public void setUserRating(int userRating) {
        this.userRating = userRating;
    }

    public double getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(double placeRating) {
        this.placeRating = placeRating;
    }

    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }

    public void setAchievedAt(LocalDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }

    public boolean isSubplace() {
        return subplace;
    }

    public void setSubplace(boolean subplace) {
        this.subplace = subplace;
    }

    public CompleteStatus getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(CompleteStatus completeStatus) {
        this.completeStatus = completeStatus;
    }
}
