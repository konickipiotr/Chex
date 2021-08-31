package com.chex.api.place;

import com.chex.modules.Coords;
import com.chex.modules.places.Place;
import com.chex.modules.places.PlaceDescriptionRepository;
import com.chex.modules.places.PlaceNameRepository;
import com.chex.modules.places.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

@Service
public class ChexPlaceService {
    private static final double latitudeOffset = 0.0012;
    private static final double longitudeOffset = 0.0012;

    private final PlaceRepository placeRepository;

    @Autowired
    public ChexPlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> checkPlace(Coords currentCoords) {
        List<Place> places = filterPlace(currentCoords);

        if(places.isEmpty())
            return null;
        List<Place> inRange = new CalculateCoords().isInRange(currentCoords, places);
        return inRange;
    }

    public List<Place> filterPlace(Coords currentCoords){
        List<Coords> newcoords = coordsWithoffset(currentCoords);
        Coords from = newcoords.get(0);
        Coords to = newcoords.get(1);

        List<Place> filtredPlace = placeRepository.filterCoords(to.latitude, from.latitude, from.longitude, to.longitude);
        return filtredPlace;
    }

    private List<Coords> coordsWithoffset(Coords currentCoords) {
        Coords coordsfrom = new Coords(currentCoords.latitude, currentCoords.longitude);
        Coords coordsTo = new Coords(currentCoords.latitude, currentCoords.longitude);

        coordsTo.latitude = BigDecimal.valueOf(currentCoords.latitude + latitudeOffset).setScale(6, RoundingMode.HALF_UP).doubleValue();
        coordsfrom.latitude = BigDecimal.valueOf(currentCoords.latitude - latitudeOffset).setScale(6, RoundingMode.HALF_UP).doubleValue();

        coordsTo.longitude = BigDecimal.valueOf(currentCoords.longitude + longitudeOffset).setScale(6, RoundingMode.HALF_UP).doubleValue();
        coordsfrom.longitude = BigDecimal.valueOf(currentCoords.longitude - longitudeOffset).setScale(6, RoundingMode.HALF_UP).doubleValue();

        List<Coords> newCoords = Arrays.asList(coordsfrom, coordsTo);
        return newCoords;
    }
}
