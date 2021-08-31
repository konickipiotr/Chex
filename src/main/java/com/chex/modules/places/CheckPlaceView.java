package com.chex.modules.places;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CheckPlaceView {

    private String id;
    private String name;
    private String description;
    private LocalDateTime timestamp;
    private String image;
    private int points;

    public CheckPlaceView(Place place) {
        this.id = place.getId();
        this.points = place.getPoints();
        this.image = place.getImgurl();
        this.timestamp = LocalDateTime.now();
    }
}
