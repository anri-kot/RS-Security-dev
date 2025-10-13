package com.rssecurity.storemanager.infra.exception;

public class UserDeniedException extends RuntimeException {
    public UserDeniedException(String message, String username) {
        super(message + " Usuário: " + username);
    }

    public UserDeniedException(String message) {
        super(message);
    }
}
