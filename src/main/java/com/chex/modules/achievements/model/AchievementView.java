package com.chex.modules.achievements.model;

import com.chex.api.profle.CompleteStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AchievementView {

    private Long id;
    private int allPlacesNum;
    private int userPlacesNum;
    private long usersReached;
    private CompleteStatus completeStatus;
    private String img;
    private String name;
    private String description;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime achievedAt;
    private int points;

    public AchievementView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getAllPlacesNum() {
        return allPlacesNum;
    }

    public void setAllPlacesNum(int allPlacesNum) {
        this.allPlacesNum = allPlacesNum;
    }

    public int getUserPlacesNum() {
        return userPlacesNum;
    }

    public void setUserPlacesNum(int userPlacesNum) {
        this.userPlacesNum = userPlacesNum;
    }

    public long getUsersReached() {
        return usersReached;
    }

    public void setUsersReached(long usersReached) {
        this.usersReached = usersReached;
    }

    public CompleteStatus getCompleteStatus() {
        return completeStatus;
    }

    public void setCompleteStatus(CompleteStatus completeStatus) {
        this.completeStatus = completeStatus;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public LocalDateTime getAchievedAt() {
        return achievedAt;
    }

    public void setAchievedAt(LocalDateTime achievedAt) {
        this.achievedAt = achievedAt;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
