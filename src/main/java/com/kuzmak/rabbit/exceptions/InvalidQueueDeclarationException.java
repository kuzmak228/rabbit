package com.kuzmak.rabbit.exceptions;

public class InvalidQueueDeclarationException extends Exception {

    public InvalidQueueDeclarationException() {
        super();
    }

    public InvalidQueueDeclarationException(String message) {
        super(message);
    }

    public InvalidQueueDeclarationException(String message, Throwable e) {
        super(message, e);
    }
}
