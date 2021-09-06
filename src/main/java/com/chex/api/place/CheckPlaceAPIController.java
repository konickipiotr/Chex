package com.chex.api.place;

import com.chex.api.AuthService;
import com.chex.api.place.response.CheckPlaceResponse;
import com.chex.api.place.service.CheckPlaceService;
import com.chex.api.place.service.ChexPlaceViewService;
import com.chex.api.post.PostService;
import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.modules.places.model.Coords;
import com.chex.modules.places.model.Place;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/checkplace")
public class CheckPlaceAPIController {

    private final CheckPlaceService checkPlaceService;
    private final ChexPlaceViewService chexPlaceViewService;
    private final AuthService authService;
    private final PostService postService;

    @Autowired
    public CheckPlaceAPIController(CheckPlaceService checkPlaceService, ChexPlaceViewService chexPlaceViewService, AuthService authService, PostService postService) {
        this.checkPlaceService = checkPlaceService;
        this.chexPlaceViewService = chexPlaceViewService;
        this.authService = authService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<CheckPlaceResponse> checkUserLocation(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, Principal principal){
        Long userid = this.authService.getUserId(principal);
        CheckPlaceResponse response = new CheckPlaceResponse();
        Coords userLocation = new Coords(latitude, longitude);

        List<Place> places = checkPlaceService.checkPlace(userLocation, userid, response);
        if(places != null && !places.isEmpty()){
            response.setCheckPlaceViewList(chexPlaceViewService.prepareListOfPlaces(places));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/finalize")
    public ResponseEntity<Void> finalizeAddPlaceToUserAccount(@RequestBody AchievedPlaceDTO achievedPlaceDTO, Principal principal){
        Long userid = this.authService.getUserId(principal);
        this.checkPlaceService.addToUserVisitedPlaces(achievedPlaceDTO, userid);
        this.postService.addNewPost(userid, achievedPlaceDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
