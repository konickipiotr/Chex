package com.chex.utils.exceptions;

public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException() {
        super("Post not found");
    }
}
