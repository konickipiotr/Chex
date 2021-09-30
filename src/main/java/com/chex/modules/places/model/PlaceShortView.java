package com.chex.modules.places.model;


public class PlaceShortView implements Comparable<PlaceShortView> {

    private String id;
    private String name;
    private String imgUrl;

    public PlaceShortView(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public PlaceShortView() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int compareTo(PlaceShortView placeShortView) {
        return -id.compareTo(placeShortView.getId());
    }
}
