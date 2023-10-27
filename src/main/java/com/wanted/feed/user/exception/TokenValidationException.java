package com.wanted.feed.user.exception;

public class TokenValidationException extends RuntimeException{

    public TokenValidationException(String message) {
        super(message);
    }
}
