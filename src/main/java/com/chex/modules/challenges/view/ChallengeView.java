package com.chex.modules.challenges.view;

import com.chex.modules.challenges.ChallengeLevel;
import com.chex.modules.challenges.model.Challenge;
import com.chex.user.ChallengeStatus;
import com.chex.user.model.UserChallenge;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

public class ChallengeView implements Comparable<ChallengeView> {
    private Long userchallengeid;
    private Long userid;
    private Long challengeid;
    private String name;
    private String description;
    private String img;
    private long timelimit;
    private ChallengeLevel level;
    private String categories;
    private long usersComplete;
    private int attemptnum;
    private int numOfCheckpoints;

    private ChallengeStatus status;
    private int currentNumOfCheckpoints;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime startTime;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime endTime;

    private List<CheckpointView> points;

    public ChallengeView() {
    }

    public ChallengeView(UserChallenge uch) {
        this.userchallengeid = uch.getChallengeid();
        this.userid = uch.getUserid();
        this.challengeid = uch.getChallengeid();
        this.attemptnum = uch.getAttemptnum();
        this.status = uch.getStatus();
        this.startTime = uch.getStartTime();
        this.endTime = uch.getEndTime();
    }

    public void fillFromChallenge(Challenge challenge) {
        this.img = challenge.getImg();
        this.level = challenge.getLevel();
        this.timelimit = challenge.getTimelimit();
        this.categories = challenge.getCategories();
    }

    public Long getUserchallengeid() {
        return userchallengeid;
    }

    public void setUserchallengeid(Long userchallengeid) {
        this.userchallengeid = userchallengeid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getChallengeid() {
        return challengeid;
    }

    public void setChallengeid(Long challengeid) {
        this.challengeid = challengeid;
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

    public long getTimelimit() {
        return timelimit;
    }

    public void setTimelimit(long timelimit) {
        this.timelimit = timelimit;
    }

    public ChallengeLevel getLevel() {
        return level;
    }

    public void setLevel(ChallengeLevel level) {
        this.level = level;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public long getUsersComplete() {
        return usersComplete;
    }

    public void setUsersComplete(long usersComplete) {
        this.usersComplete = usersComplete;
    }

    public int getAttemptnum() {
        return attemptnum;
    }

    public void setAttemptnum(int attemptnum) {
        this.attemptnum = attemptnum;
    }

    public int getNumOfCheckpoints() {
        return numOfCheckpoints;
    }

    public void setNumOfCheckpoints(int numOfCheckpoints) {
        this.numOfCheckpoints = numOfCheckpoints;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

    public int getCurrentNumOfCheckpoints() {
        return currentNumOfCheckpoints;
    }

    public void setCurrentNumOfCheckpoints(int currentNumOfCheckpoints) {
        this.currentNumOfCheckpoints = currentNumOfCheckpoints;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<CheckpointView> getPoints() {
        return points;
    }

    public void setPoints(List<CheckpointView> points) {
        this.points = points;
    }

    @Override
    public int compareTo(ChallengeView o) {
        int name = this.name.compareTo(o.getName());
        if(name != 0)
            return name;
        return this.status.equals(o.getStatus()) ? 1 : -1;
    }
}
