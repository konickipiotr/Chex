package com.chex.modules.places;

import com.chex.modules.places.model.Place;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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
        this.image = place.getImgurl();
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public int compareTo(CheckPlaceView checkPlaceView) {
        return this.id.compareTo(checkPlaceView.id);
    }
}
