package org.lesson06.repository.exception;

public class EntityNotFound extends Throwable {
    public EntityNotFound(String errorMessage) {
        super(errorMessage);
    }
}