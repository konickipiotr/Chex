package com.chex.modules.places.model;

import lombok.Data;

@Data
public class Coords {
    public double latitude;
    public double longitude;

    public Coords(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
