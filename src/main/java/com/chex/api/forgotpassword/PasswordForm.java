package com.chex.api.forgotpassword;

import lombok.Data;

@Data
public class PasswordForm {
    private String code;
    private String password1;
    private String password2;

    public boolean passwordsAreNotTheSame(){
        return !password1.equals(password2);
    }
}
