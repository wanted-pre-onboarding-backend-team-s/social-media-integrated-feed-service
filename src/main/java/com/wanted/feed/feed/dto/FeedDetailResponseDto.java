package com.wanted.feed.feed.dto;

import com.wanted.feed.feed.domain.Feed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FeedDetailResponseDto {

    private String type;
    private String title;
    private String content;
    private int viewCount;
    private String contentId;

    public static FeedDetailResponseDto of(Feed feed) {
        return new FeedDetailResponseDto(
            feed.getType(),
            feed.getTitle(),
            feed.getContent(),
            feed.getViewCount(),
            feed.getContentId()
        );
    }

}
