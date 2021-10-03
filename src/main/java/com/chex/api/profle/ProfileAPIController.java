package com.chex.api.profle;

import com.chex.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
public class ProfileAPIController {

    private ProfileService profileService;
    private AuthService authService;

    @Autowired
    public ProfileAPIController(ProfileService profileService, AuthService authService) {
        this.profileService = profileService;
        this.authService = authService;
    }

    @GetMapping("{id}")
    public ResponseEntity<ProfileResponse> getProfileView(@PathVariable("id")String id, Principal principal){
        Long userId = authService.getUserId(principal);
        ProfileResponse profileInfo = this.profileService.getProfileInfo(userId, id);
        return new ResponseEntity<>(profileInfo, HttpStatus.OK);
    }
}
