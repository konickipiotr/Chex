package com.chex.files;

public class FailedSaveFileException extends RuntimeException {
    public FailedSaveFileException() {
        super("ChexApp Exception: Failed to save file");
    }
}
