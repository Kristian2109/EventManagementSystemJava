package com.projects.eventsbook.exceptions;

public class NoEntityFoundException extends RuntimeException{
    public NoEntityFoundException() {
    }

    public NoEntityFoundException(String message) {
        super(message);
    }

    public NoEntityFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEntityFoundException(Throwable cause) {
        super(cause);
    }
}
