package com.chex.api.place;

import com.chex.modules.Coords;
import com.chex.modules.places.CheckPlaceView;
import com.chex.modules.places.Place;
import com.chex.utils.ListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkplace")
public class CheckPlaceAPIController {

    private final ChexPlaceService chexPlaceService;
    private final ChexPlaceViewService chexPlaceViewService;

    @Autowired
    public CheckPlaceAPIController(ChexPlaceService chexPlaceService, ChexPlaceViewService chexPlaceViewService) {
        this.chexPlaceService = chexPlaceService;
        this.chexPlaceViewService = chexPlaceViewService;
    }

    @GetMapping
    public ResponseEntity<ListWrapper> checkUserLocation(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude){
        Coords userLocation = new Coords(latitude, longitude);
        List<Place> places = chexPlaceService.checkPlace(userLocation);
        List<CheckPlaceView> checkPlaceViews = chexPlaceViewService.prepareListOfPlaces(places);

        if(checkPlaceViews.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(new ListWrapper(checkPlaceViews), HttpStatus.OK);
    }

    @PostMapping("/finalize")
    public ResponseEntity<Void> finalizeAddPlaceToUserAccount(@RequestBody AchievedPlaceDTO achievedPlaceDTO){
        System.out.println(achievedPlaceDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
