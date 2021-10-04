package com.chex.api.place;

import com.chex.api.place.service.CalculateCoords;
import com.chex.modules.places.model.Coords;
import com.chex.modules.places.model.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculateCoordsTest {

    Place fredroMonumen = new Place();

    @BeforeEach
    void setUp() {
        fredroMonumen.setId("FREDRO");
        fredroMonumen.setLatitude(51.10966433078471);
        fredroMonumen.setLongitude(17.031311853885065);
        fredroMonumen.setRadius(10);
    }
    
    @Test
    void users_in_area() {

        List<Place> placesInArea = Collections.singletonList(fredroMonumen);

        Coords userSouth = new Coords(51.10960527, 17.03132156);
        List<Place> inRange = CalculateCoords.isInRange(userSouth, placesInArea);
        assertFalse(inRange.isEmpty());

        Coords userNorth = new Coords(51.10973968865038, 17.031362145300008);
        inRange = CalculateCoords.isInRange(userNorth, placesInArea);
        assertFalse(inRange.isEmpty());

        Coords userEast = new Coords(51.10963528218874, 17.031445293777136);
        inRange = CalculateCoords.isInRange(userEast, placesInArea);
        assertFalse(inRange.isEmpty());

        Coords userWest = new Coords(51.10968874843023, 17.03118646062822);
        inRange = CalculateCoords.isInRange(userWest, placesInArea);
        assertFalse(inRange.isEmpty());
    }

    @Test
    void users_out_of_area() {

        List<Place> placesInArea = Collections.singletonList(fredroMonumen);

        Coords userSouth = new Coords(51.109578868922036, 17.03125217474375);
        List<Place> inRange = CalculateCoords.isInRange(userSouth, placesInArea);
        assertTrue(inRange.isEmpty());

        Coords userNorth = new Coords(51.109754844407014, 17.03137019193082);
        inRange = CalculateCoords.isInRange(userNorth, placesInArea);
        assertTrue(inRange.isEmpty());

        Coords userEast = new Coords(51.10963149324018, 17.031459375373316);
        inRange = CalculateCoords.isInRange(userEast, placesInArea);
        assertTrue(inRange.isEmpty());

        Coords userWest = new Coords(51.10969380035535, 17.031165673510042);
        inRange = CalculateCoords.isInRange(userWest, placesInArea);
        assertTrue(inRange.isEmpty());
    }
}