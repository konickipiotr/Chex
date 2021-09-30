package com.chex.user.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UsersAchievementsInProgress {

    @Id
    @SequenceGenerator(name = "userachievementsinprogress_sequence", sequenceName = "userachievementsinprogress_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userachievementsinprogress_sequence")
    private Long id;
    private Long userid;
    private Long achievementid;
    private String placeid;
    private LocalDateTime achievedat;

    public UsersAchievementsInProgress() {
    }

    public UsersAchievementsInProgress(Long userid, Long achievementid, String placeid) {
        this.userid = userid;
        this.achievementid = achievementid;
        this.placeid = placeid;
    }

    public UsersAchievementsInProgress(Long userid, Long achievementid, String placeid, LocalDateTime achievedat) {
        this.userid = userid;
        this.achievementid = achievementid;
        this.placeid = placeid;
        this.achievedat = achievedat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
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

    public LocalDateTime getAchievedat() {
        return achievedat;
    }

    public void setAchievedat(LocalDateTime achievedat) {
        this.achievedat = achievedat;
    }
}
