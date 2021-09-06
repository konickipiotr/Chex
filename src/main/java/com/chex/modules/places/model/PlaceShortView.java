package com.chex.modules.places.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceShortView implements Comparable<PlaceShortView> {

    private String id;
    private String name;

    @Override
    public int compareTo(PlaceShortView placeShortView) {
        return -id.compareTo(placeShortView.getId());
    }
}
