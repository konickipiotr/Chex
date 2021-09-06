package com.chex.utils.exceptions;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long id) {
        super("Not found user with id: " + id);
    }
}
