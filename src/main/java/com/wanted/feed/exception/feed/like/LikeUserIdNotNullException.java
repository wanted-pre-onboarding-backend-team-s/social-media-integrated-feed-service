package com.wanted.feed.exception.feed.like;

public class LikeUserIdNotNullException extends IllegalArgumentException {

    public LikeUserIdNotNullException() {
        super("userId 는 Null 일 수 없습니다.");
    }
}
