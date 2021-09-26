package com.chex.utils.exceptions;

public class FailedSaveFileException extends RuntimeException {
    public FailedSaveFileException() {
        super("ChexApp Exception: Failed to save file");
    }
}
