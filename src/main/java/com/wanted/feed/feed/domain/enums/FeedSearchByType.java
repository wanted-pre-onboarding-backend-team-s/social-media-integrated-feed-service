package com.wanted.feed.feed.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedSearchByType {

    TITLE("title", "타이틀 기준"),
    CONTENT("content", "내용 기준"),
    TITLE_AND_CONTENT("title,content", "타이틀, 내용 기준");

    private final String name;
    private final String description;

}
