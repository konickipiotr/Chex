package com.chex.webapp.admin.places.newplace;

import org.springframework.web.multipart.MultipartFile;

public class PlaceForm {

    private String placeid;
    private String prefix;
    private String suffix;
    private boolean subplace;
    private String nameEng;
    private String namePl;
    private boolean sameAsFirst;
    private double longitude;
    private double latitude;
    private double radius;
    private Long category;
    private int points;
    private int difficultylevel;
    private String descriptionEng;
    private String descriptionPl;
    private MultipartFile picture;
    private String svgPath;

    public PlaceForm() {
    }

    public PlaceForm(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        if(prefix.length()<11)
            subplace = true;
    }

    public String createId(){
        return prefix + "." + placeid + suffix;
    }

    public String getParentId(){
        return prefix + ".000" + suffix;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public boolean isSubplace() {
        return subplace;
    }

    public void setSubplace(boolean subplace) {
        this.subplace = subplace;
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

    public boolean isSameAsFirst() {
        return sameAsFirst;
    }

    public void setSameAsFirst(boolean sameAsFirst) {
        this.sameAsFirst = sameAsFirst;
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

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDifficultylevel() {
        return difficultylevel;
    }

    public void setDifficultylevel(int difficultylevel) {
        this.difficultylevel = difficultylevel;
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

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }

    public String getSvgPath() {
        return svgPath;
    }

    public void setSvgPath(String svgPath) {
        this.svgPath = svgPath;
    }
}
