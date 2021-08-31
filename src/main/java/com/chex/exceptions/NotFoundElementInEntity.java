package com.chex.exceptions;

public class NotFoundElementInEntity extends RuntimeException{
    public NotFoundElementInEntity(String id, Object specClass) {
        super("Not found element id = " + id + " in " + specClass.getClass().getName());
    }
}
