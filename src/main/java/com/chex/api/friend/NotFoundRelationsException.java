package com.chex.api.friend;

public class NotFoundRelationsException extends RuntimeException{

    public NotFoundRelationsException(Long user1, Long user2) {
        super("Not found relations user id: " + user1 + " -> " + user2);
    }
}
