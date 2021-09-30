package com.chex.modules.achievements.model;

import javax.persistence.*;

@Entity
public class AchievementPlaces {

    @Id
    @SequenceGenerator(name = "achievementplaces_sequence", sequenceName = "achievementplaces_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "achievementplaces_sequence")
    private Long id;
    private Long achievementid;
    private String placeid;

    public AchievementPlaces(Long achievementid, String placeid) {
        this.achievementid = achievementid;
        this.placeid = placeid;
    }

    public AchievementPlaces() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAchievementid() {
        return achievementid;
    }

    public void setAchievementid(Long achievementid) {
        this.achievementid = achievementid;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }
}
