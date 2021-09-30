package com.chex.modules.achievements.model;

import javax.persistence.*;

@Entity
public class AchievementName {

    @Id
    private Long id;
    private String pl;
    private String eng;

    public AchievementName(Long id, String pl, String eng) {
        this.id = id;
        this.pl = pl;
        this.eng = eng;
    }

    public AchievementName() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }
}
