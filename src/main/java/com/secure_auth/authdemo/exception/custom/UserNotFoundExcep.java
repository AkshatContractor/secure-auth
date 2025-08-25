package com.secure_auth.authdemo.exception.custom;

public class UserNotFoundExcep extends RuntimeException {
    public UserNotFoundExcep(String message) {
        super(message);
    }
}
