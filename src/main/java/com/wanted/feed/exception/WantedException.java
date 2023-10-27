package com.wanted.feed.exception;

import lombok.Getter;

@Getter
public class WantedException extends RuntimeException{
    private final ErrorType errorType = ErrorType.of(this.getClass());
}