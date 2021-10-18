package com.chex.modules.challenges.model;

import com.chex.modules.challenges.CheckpointType;

import javax.persistence.*;

@Entity
public class ChallengePoint{

    @Id
    @SequenceGenerator(name = "challenge_point_sequence", sequenceName = "challenge_point_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "challenge_point_sequence")
    private Long id;
    private Long challengeid;
    private int seq;
    private String name;
    @Enumerated(EnumType.STRING)
    private CheckpointType checkpointtype;

    private boolean isplace;
    private String placeid;
    private double longitude;
    private double latitude;
    private double radius;


    public ChallengePoint() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChallengeid() {
        return challengeid;
    }

    public void setChallengeid(Long challengeid) {
        this.challengeid = challengeid;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CheckpointType getCheckpointtype() {
        return checkpointtype;
    }

    public void setCheckpointtype(CheckpointType checkpointtype) {
        this.checkpointtype = checkpointtype;
    }

    public boolean isIsplace() {
        return isplace;
    }

    public void setIsplace(boolean isplace) {
        this.isplace = isplace;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
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
}
