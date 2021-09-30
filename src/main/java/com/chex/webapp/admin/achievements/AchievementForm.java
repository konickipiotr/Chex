package com.chex.webapp.admin.achievements;

import org.springframework.web.multipart.MultipartFile;

public class AchievementForm {
    private String namepl;
    private String nameen;
    private int points;
    private String descriptionen;
    private String descriptionpl;
    private String[] achievementPlaces;
    private MultipartFile picture;

    public AchievementForm() {
    }

    public String getNamepl() {
        return namepl;
    }

    public void setNamepl(String namepl) {
        this.namepl = namepl;
    }

    public String getNameen() {
        return nameen;
    }

    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDescriptionen() {
        return descriptionen;
    }

    public void setDescriptionen(String descriptionen) {
        this.descriptionen = descriptionen;
    }

    public String getDescriptionpl() {
        return descriptionpl;
    }

    public void setDescriptionpl(String descriptionpl) {
        this.descriptionpl = descriptionpl;
    }

    public String[] getAchievementPlaces() {
        return achievementPlaces;
    }

    public void setAchievementPlaces(String[] achievementPlaces) {
        this.achievementPlaces = achievementPlaces;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
