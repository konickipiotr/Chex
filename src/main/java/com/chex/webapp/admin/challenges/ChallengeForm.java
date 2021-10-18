package com.chex.webapp.admin.challenges;

import com.chex.modules.challenges.ChallengeLevel;

public class ChallengeForm {

    private String nameEng;
    private String namePl;
    private String descriptionEng;
    private String descriptionPl;
    private ChallengeLevel level;
    private boolean sameAsEng;
    private int points;
    private int years;
    private int months;
    private int days;
    private int hours;
    private int minutes;
    private int seconds;
    private String imgTemp;

    public ChallengeForm() {
        this.points = 0;
        this.years = 0;
        this.months = 0;
        this.days = 0;
        this.hours = 0;
        this.minutes = 0;
        this.seconds = 0;
    }

    public void setSame(){
        if(sameAsEng)
            namePl = nameEng;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public String getNamePl() {
        return namePl;
    }

    public void setNamePl(String namePl) {
        this.namePl = namePl;
    }

    public String getDescriptionEng() {
        return descriptionEng;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
    }

    public String getDescriptionPl() {
        return descriptionPl;
    }

    public void setDescriptionPl(String descriptionPl) {
        this.descriptionPl = descriptionPl;
    }

    public boolean isSameAsEng() {
        return sameAsEng;
    }

    public void setSameAsEng(boolean sameAsEng) {
        this.sameAsEng = sameAsEng;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getImgTemp() {
        return imgTemp;
    }

    public void setImgTemp(String imgTemp) {
        this.imgTemp = imgTemp;
    }

    public ChallengeLevel getLevel() {
        return level;
    }

    public void setLevel(ChallengeLevel level) {
        this.level = level;
    }
}
