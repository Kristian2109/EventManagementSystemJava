package com.projects.eventsbook.exceptions;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException() {
    }

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateResourceException(Throwable cause) {
        super(cause);
    }
}
