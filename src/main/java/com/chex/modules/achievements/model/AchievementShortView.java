package com.chex.modules.achievements.model;

public class AchievementShortView implements Comparable<AchievementShortView> {

    private Long id;
    private String name;
    private String img;
    private int points;

    public AchievementShortView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int compareTo(AchievementShortView achievementShortView) {
        return this.name.compareTo(achievementShortView.name);
    }
}
