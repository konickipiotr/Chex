package com.chex.api.forgotpassword;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forgotpassword")
public class ForgotPasswordControler {

    public ResponseEntity<Void> resetPassword(@RequestBody String email){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
