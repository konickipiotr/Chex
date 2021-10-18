package com.chex.api.place.response;

import com.chex.modules.achievements.model.AchievementShortView;
import com.chex.modules.CheckPlaceView;
import com.chex.modules.challenges.view.ChallengeShortView;
import com.chex.modules.challenges.view.CheckpointView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CheckPlaceResponse {
    private CheckPlaceResponseStatus responseStatus;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;
    private List<CheckPlaceView> checkPlaceViewList = new ArrayList<>();
    private List<AchievementShortView> achievementShortViews = new ArrayList<>();
    private List<CheckpointView> checkpointViewList = new ArrayList<>();
    private List<ChallengeShortView> userShortChallengeViews = new ArrayList<>();

    public CheckPlaceResponse() {
    }

    public CheckPlaceResponse(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public CheckPlaceResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(CheckPlaceResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<CheckPlaceView> getCheckPlaceViewList() {
        return checkPlaceViewList;
    }

    public void setCheckPlaceViewList(List<CheckPlaceView> checkPlaceViewList) {
        this.checkPlaceViewList = checkPlaceViewList;
    }

    public List<AchievementShortView> getAchievementShortViews() {
        return achievementShortViews;
    }

    public void setAchievementShortViews(List<AchievementShortView> achievementShortViews) {
        this.achievementShortViews = achievementShortViews;
    }

    public List<CheckpointView> getCheckpointViewList() {
        return checkpointViewList;
    }

    public void setCheckpointViewList(List<CheckpointView> checkpointViewList) {
        this.checkpointViewList = checkpointViewList;
    }

    public List<ChallengeShortView> getUserShortChallengeViews() {
        return userShortChallengeViews;
    }

    public void setUserShortChallengeViews(List<ChallengeShortView> userShortChallengeViews) {
        this.userShortChallengeViews = userShortChallengeViews;
    }
}
