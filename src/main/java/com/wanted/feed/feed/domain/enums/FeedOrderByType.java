package com.wanted.feed.feed.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedOrderByType {

    CREATED_AT("created_at", "최신순"),
    UPDATED_AT("updated_at", "업데이트순"),
    LIKE_COUNT("like_count", "좋아요순"),
    SHARE_COUNT("share_count", "공유순"),
    VIEW_COUNT("view_count", "조회수순");

    private final String name;
    private final String description;

}
