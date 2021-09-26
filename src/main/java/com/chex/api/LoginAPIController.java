package com.chex.api;

import com.chex.authentication.Auth;
import com.chex.authentication.AuthRepository;
import com.chex.user.User;
import com.chex.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Optional;

@RestController
public class LoginAPIController {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    @Autowired
    public LoginAPIController(AuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/api/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Auth> loginAndGetAuth(Principal principal){
        Optional<Auth> oAuth = this.authRepository.findByUsername(principal.getName());
        if(oAuth.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(oAuth.get(), HttpStatus.OK);
    }

    @GetMapping("/api/user")
    public ResponseEntity<User> getUser(Principal principal){
        Optional<Auth> oAuth = this.authRepository.findByUsername(principal.getName());
        User user = this.userRepository.findById(oAuth.get().getId()).get();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
