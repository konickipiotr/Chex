package com.chex.api.place.service;

import com.chex.api.place.service.PlaceNameService;
import com.chex.modules.places.*;
import com.chex.modules.places.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChexPlaceViewService {

    private final PlaceNameService placeNameService;

    @Autowired
    public ChexPlaceViewService(PlaceNameService placeNameService) {
        this.placeNameService = placeNameService;
    }

    public List<CheckPlaceView> prepareListOfPlaces(List<Place> placeList){
        List<CheckPlaceView>  resultList = new ArrayList<>();

        for(Place p : placeList)
            resultList.add(prepareSinglePlace(p));

        Collections.sort(resultList);
        Collections.reverse(resultList);
        return resultList;
    }

    private CheckPlaceView prepareSinglePlace(Place place){
        CheckPlaceView pv = new CheckPlaceView(place);
        pv.setDescription(placeNameService.getDescription(place.getId()));
        pv.setName(placeNameService.getName(place.getId()));
        return pv;
    }
}
