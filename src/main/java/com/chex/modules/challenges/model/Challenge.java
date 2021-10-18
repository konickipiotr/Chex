package com.chex.modules.challenges.model;

import com.chex.modules.challenges.ChallengeLevel;
import com.chex.utils.DateUtils;
import com.chex.utils.Duration;
import com.chex.webapp.admin.challenges.ChallengeForm;

import javax.persistence.*;

@Entity
public class Challenge {

    @Id
    @SequenceGenerator(name = "challenge_sequence", sequenceName = "challenge_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "challenge_sequence")
    private Long id;
    private int points;
    private String img;
    private long timelimit;
    @Enumerated(EnumType.STRING)
    private ChallengeLevel level;
    private String categories;

    public Challenge() {
        this.level = ChallengeLevel.NORMAL;
    }

    public void setTimelimit(ChallengeForm form){
        Duration duration = new Duration();
        duration.setDays((form.getYears() * 365) + (form.getMonths() * 30) + form.getDays());
        duration.setHours(form.getHours());
        duration.setMinutes(form.getMinutes());
        duration.setSeconds(form.getSeconds());
        this.timelimit = DateUtils.convertDurationToMillis(duration);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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
}
