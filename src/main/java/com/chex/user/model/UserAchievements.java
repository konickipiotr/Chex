package com.chex.user.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserAchievements {

    @Id
    @SequenceGenerator(name = "userachievements_sequence", sequenceName = "userachievements_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userachievements_sequence")
    private Long id;
    private Long userid;
    private Long achievementid;
    private LocalDateTime achievedat;

    public UserAchievements(Long userid, Long achievementid, LocalDateTime achievedat) {
        this.userid = userid;
        this.achievementid = achievementid;
        this.achievedat = achievedat;
    }

    public UserAchievements() {
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

    public LocalDateTime getAchievedat() {
        return achievedat;
    }

    public void setAchievedat(LocalDateTime achievedat) {
        this.achievedat = achievedat;
    }
}
