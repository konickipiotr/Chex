package com.chex.api.place;

import com.chex.modules.places.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChexPlaceViewService {

    private final PlaceNameService placeNameService;

    @Autowired
    public ChexPlaceViewService(PlaceNameService placeNameService) {
        this.placeNameService = placeNameService;
    }

    public List<CheckPlaceView> prepareListOfPlaces(List<Place> placeList){
        List<CheckPlaceView>  resutlList = new ArrayList<>();
        if(placeList == null || placeList.isEmpty())
            return resutlList;

        for(Place p : placeList)
            resutlList.add(prepareSinglePlace(p));

        return resutlList;
    }

    private CheckPlaceView prepareSinglePlace(Place place){
        CheckPlaceView pv = new CheckPlaceView(place);
        pv.setDescription(placeNameService.getDescription(place.getId()));
        pv.setName(placeNameService.getName(place.getId()));
        return pv;
    }
}
