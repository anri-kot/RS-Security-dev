package com.rssecurity.storemanager.exception;

public class ResourceAlreadyExists extends RuntimeException {
    public ResourceAlreadyExists(String message) {
        super(message);
    }
}
