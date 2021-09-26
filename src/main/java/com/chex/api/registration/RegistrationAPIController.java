package com.chex.api.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationAPIController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationAPIController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Void> registerUser(@RequestBody RegistrationForm form){
        return registrationService.registerUser(form);
    }

    @GetMapping(value = "/{code}")
    public ResponseEntity<Void> activateAccount(@PathVariable("code")String code){
        return registrationService.activateAccount(code);
    }
}
