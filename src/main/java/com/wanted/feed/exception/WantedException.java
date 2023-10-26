package com.wanted.feed.exception;

import lombok.Getter;

@Getter
public class WantedException extends RuntimeException{
    private final ErrorType errorType;

    public WantedException(ErrorType errorType) {
        this.errorType = errorType;
    }
}