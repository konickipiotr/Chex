package com.chex.api.registration;

import lombok.Data;

@Data
public class RegistrationForm {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String gender;
    private String country;
}
