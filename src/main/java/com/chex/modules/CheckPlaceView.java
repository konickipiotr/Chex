package com.chex.modules;

import com.chex.modules.places.model.Place;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckPlaceView implements Serializable, Comparable<CheckPlaceView> {

    private String id;
    private String name;
    private String description;
    @JsonSerialize(using = ToStringSerializer.class)
    private LocalDateTime timestamp;
    private String image;
    private int points;

    public CheckPlaceView() {
    }

    public CheckPlaceView(Place place) {
        this.id = place.getId();
        this.points = place.getPoints();
        this.image = place.getImg();
        this.timestamp = LocalDateTime.now();
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int compareTo(CheckPlaceView checkPlaceView) {
        return this.id.compareTo(checkPlaceView.id);
    }
}
