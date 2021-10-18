package com.chex.modules.challenges.view;

import com.chex.modules.challenges.ChallengeLevel;
import com.chex.modules.challenges.model.Challenge;


public class ChallengeShortView {

    private long id;
    private long userchallengeid;
    private String name;
    private String img;
    private long usersComplete;
    private ChallengeLevel level;
    private String category;

    public ChallengeShortView() {
    }

    public ChallengeShortView(Challenge ch) {
        this.id = ch.getId();
        this.img = ch.getImg();
        this.level = ch.getLevel();
        this.category = ch.getCategories();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserchallengeid() {
        return userchallengeid;
    }

    public void setUserchallengeid(long userchallengeid) {
        this.userchallengeid = userchallengeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public long getUsersComplete() {
        return usersComplete;
    }

    public void setUsersComplete(long usersComplete) {
        this.usersComplete = usersComplete;
    }

    public ChallengeLevel getLevel() {
        return level;
    }

    public void setLevel(ChallengeLevel level) {
        this.level = level;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
