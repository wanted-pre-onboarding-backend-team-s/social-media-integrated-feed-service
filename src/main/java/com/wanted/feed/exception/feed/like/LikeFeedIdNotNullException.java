package com.wanted.feed.exception.feed.like;

public class LikeFeedIdNotNullException extends IllegalArgumentException {

    public LikeFeedIdNotNullException() {
        super("feedId 는 Null 일 수 없습니다.");
    }
}
