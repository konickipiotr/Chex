package com.chex.api.user;

import com.chex.api.AuthService;
import com.chex.user.model.User;
import com.chex.utils.StringHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class UserAPIController {

    private AuthService authService;
    private ProfilePhotoService profilePhotoService;

    @Autowired
    public UserAPIController(AuthService authService, ProfilePhotoService profilePhotoService) {
        this.authService = authService;
        this.profilePhotoService = profilePhotoService;
    }

    @GetMapping
    public ResponseEntity<User> getUser(Principal principal){
        User user = authService.getUser(principal);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/profilephoto")
    public ResponseEntity<Void> deleteProfilePhoto(Principal principal){
        Long userId = this.authService.getUserId(principal);
        profilePhotoService.deleteProfilePhoto(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/profilephoto")
    public ResponseEntity<Void> setNewProfilePhoto(@RequestBody StringHolder stringPhoto, Principal principal){
        Long userId = this.authService.getUserId(principal);
        profilePhotoService.setNewProfilePhoto(stringPhoto.getValue(), userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
