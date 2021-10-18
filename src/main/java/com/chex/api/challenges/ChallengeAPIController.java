package com.chex.api.challenges;

import com.chex.api.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/challenge")
public class ChallengeAPIController {

    private final ChallengeAPIService challengeAPIService;
    private final AuthService authService;

    public ChallengeAPIController(ChallengeAPIService challengeAPIService, AuthService authService) {
        this.challengeAPIService = challengeAPIService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<UserChallengeResponse> getUserChallengesInfo(Principal principal){
        Long userId = authService.getUserId(principal);
        return challengeAPIService.getUserChallengesInfo(userId);
    }

    @PostMapping("/select")
    public ResponseEntity<UserChallengeResponse> selectChallenge(@RequestBody Long challengeId, Principal principal){
        Long userId = authService.getUserId(principal);
        return challengeAPIService.selectChallenge(challengeId, userId);
    }

    @PostMapping("/removeuserchallenge")
    public ResponseEntity<UserChallengeResponse> removeUserChallenge(@RequestBody Long challengeId, Principal principal){
        Long userId = authService.getUserId(principal);
        return challengeAPIService.removeUserChallenge(challengeId, userId);
    }


    @GetMapping("/available")
    public ResponseEntity<UserChallengeResponse> getListOfAvailableChallenges(Principal principal){
        Long userId = authService.getUserId(principal);
        return challengeAPIService.getListOfAvailableChallenges(userId);
    }

}
