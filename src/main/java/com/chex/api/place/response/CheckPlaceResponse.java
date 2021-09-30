package com.chex.api.place.response;

import com.chex.modules.achievements.model.AchievementShortView;
import com.chex.modules.CheckPlaceView;
import java.util.List;

public class CheckPlaceResponse {
    private CheckPlaceResponseStatus responseStatus;
    private List<CheckPlaceView> checkPlaceViewList;
    private List<AchievementShortView> achievementShortViews;

    public CheckPlaceResponse() {
    }

    public CheckPlaceResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(CheckPlaceResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
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
}
