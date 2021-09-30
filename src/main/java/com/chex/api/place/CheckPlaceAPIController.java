package com.chex.api.place;

import com.chex.api.AuthService;
import com.chex.api.place.response.CheckPlaceResponse;
import com.chex.api.place.service.AchievementAPIService;
import com.chex.api.place.service.CheckPlaceService;
import com.chex.api.place.service.ChexPlaceViewService;
import com.chex.api.post.PostService;
import com.chex.modules.places.model.Coords;
import com.chex.modules.places.model.Place;
import com.chex.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/checkplace")
public class CheckPlaceAPIController {

    private final CheckPlaceService checkPlaceService;
    private final ChexPlaceViewService chexPlaceViewService;
    private final AuthService authService;
    private final PostService postService;
    private final AchievementAPIService achievementAPIService;

    @Autowired
    public CheckPlaceAPIController(CheckPlaceService checkPlaceService, ChexPlaceViewService chexPlaceViewService, AuthService authService, PostService postService, AchievementAPIService achievementAPIService) {
        this.checkPlaceService = checkPlaceService;
        this.chexPlaceViewService = chexPlaceViewService;
        this.authService = authService;
        this.postService = postService;
        this.achievementAPIService = achievementAPIService;
    }

    @GetMapping
    public ResponseEntity<CheckPlaceResponse> checkUserLocation(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude, Principal principal){
        Long userid = this.authService.getUserId(principal);
        CheckPlaceResponse response = new CheckPlaceResponse();
        Coords userLocation = new Coords(latitude, longitude);

        List<Place> places = checkPlaceService.checkPlace(userLocation, userid, response);
        if(places != null && !places.isEmpty()){
            response.setCheckPlaceViewList(chexPlaceViewService.prepareListOfPlaces(places));
            response.getCheckPlaceViewList().stream().map(i -> i.getId()).collect(Collectors.toSet());
            response.setAchievementShortViews(achievementAPIService.checkAchievement(response
                    .getCheckPlaceViewList()
                    .stream()
                    .map(i -> i.getId())
                    .collect(Collectors.toSet()),
                    userid));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/finalize")
    public ResponseEntity<Void> finalizeAddPlaceToUserAccount(@RequestBody AchievedPlaceDTO achievedPlaceDTO, Principal principal){
        User user = this.authService.getUser(principal);
        int exp = this.checkPlaceService.addToUserVisitedPlaces(achievedPlaceDTO, user.getId());
        exp += this.achievementAPIService.addPlaceToUserAchievements(achievedPlaceDTO.getAchievedPlaces().keySet(), user.getId());
        user.addExp(exp);
        this.postService.addNewPost(user, achievedPlaceDTO);
        this.authService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
