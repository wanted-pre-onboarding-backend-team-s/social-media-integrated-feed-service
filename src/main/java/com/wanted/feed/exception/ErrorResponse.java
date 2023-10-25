package com.wanted.feed.exception;


public record ErrorResponse(
    String code,
    String message
){
    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse of(ErrorType errorType) {
        return new ErrorResponse(errorType.getCode(), errorType.getMessage());
    }
}
