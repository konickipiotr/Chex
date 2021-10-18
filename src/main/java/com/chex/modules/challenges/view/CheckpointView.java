package com.chex.modules.challenges.view;

import com.chex.modules.challenges.CheckpointType;
import com.chex.modules.challenges.model.ChallengePoint;
import com.chex.user.ChallengeStatus;
import com.chex.user.model.UserChallengePoint;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class CheckpointView implements Comparable<CheckpointView> {

    private Long userpointid;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime reachedat;
    private String challengeName;
    private String name;
    private int seq;
    private String img;
    private CheckpointType checkpointtype;
    private ChallengeStatus status;

    private double longitude;
    private double latitude;
    private double radius;

    public CheckpointView() {
    }

    public CheckpointView(UserChallengePoint point) {
        this.userpointid = point.getId();
        this.reachedat = point.getReachedat();
        this.status = point.getStatus();
        this.checkpointtype = point.getCheckpointtype();
    }

    public Long getUserpointid() {
        return userpointid;
    }

    public void setUserpointid(Long userpointid) {
        this.userpointid = userpointid;
    }

    public LocalDateTime getReachedat() {
        return reachedat;
    }

    public void setReachedat(LocalDateTime reachedat) {
        this.reachedat = reachedat;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public CheckpointType getCheckpointtype() {
        return checkpointtype;
    }

    public void setCheckpointtype(CheckpointType checkpointtype) {
        this.checkpointtype = checkpointtype;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
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

    public void fillCoordsFromChallengePoint(ChallengePoint challengePoint) {
        this.latitude = challengePoint.getLatitude();
        this.longitude = challengePoint.getLongitude();
        this.radius = challengePoint.getRadius();
        this.name = challengePoint.getName();
    }

    @Override
    public int compareTo(CheckpointView o) {
        return this.seq <= o.seq ? 1 : -1;
    }
}
