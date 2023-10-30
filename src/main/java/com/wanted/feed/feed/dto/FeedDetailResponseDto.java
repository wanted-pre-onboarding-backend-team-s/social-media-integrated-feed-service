package com.wanted.feed.feed.dto;

import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.Hashtag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FeedDetailResponseDto {

    private List<String> hashtag;
    private String type;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int shareCount;
    private String contentId;

    public static FeedDetailResponseDto of(Feed feed, List<Hashtag> hashtagList) {
        List<String> hashtags = toHashtags(hashtagList);

        return new FeedDetailResponseDto(
            hashtags,
            feed.getType(),
            feed.getTitle(),
            feed.getContent(),
            feed.getViewCount(),
            feed.getLikeCount(),
            feed.getShareCount(),
            feed.getContentId()
        );
    }

    private static List<String> toHashtags(List<Hashtag> hashtagList) {
        return hashtagList.stream()
            .map(Hashtag::getName)
            .collect(Collectors.toList());
    }

}
