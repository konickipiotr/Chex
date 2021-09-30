package com.chex.modules.achievements.model;

import javax.persistence.*;

@Entity
public class Achievement {

    @Id
    @SequenceGenerator(name = "achievement_sequence", sequenceName = "achievement_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "achievement_sequence")
    private Long id;
    private int points;
    private String imgurl;
    private String imgpath;

    public Achievement() {
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
}
