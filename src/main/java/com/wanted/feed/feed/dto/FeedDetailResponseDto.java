package com.wanted.feed.feed.dto;

import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.Hashtag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedDetailResponseDto {

    private List<String> hashtag;
    private String type;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int shareCount;
    private String contentId;

    @Builder
    public FeedDetailResponseDto(List<String> hashtag, String type, String title, String content,
            int viewCount, int likeCount, int shareCount, String contentId) {
        this.hashtag = hashtag;
        this.type = type;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.shareCount = shareCount;
        this.contentId = contentId;
    }

    public static FeedDetailResponseDto of(Feed feed, List<Hashtag> hashtagList) {
        List<String> hashtags = toHashtags(hashtagList);

        return FeedDetailResponseDto.builder()
                .hashtag(hashtags)
                .type(feed.getType())
                .title(feed.getTitle())
                .content(feed.getContent())
                .viewCount(feed.getViewCount())
                .likeCount(feed.getLikeCount())
                .shareCount(feed.getShareCount())
                .contentId(feed.getContentId())
                .build();
    }

    private static List<String> toHashtags(List<Hashtag> hashtagList) {
        return hashtagList.stream()
                .map(Hashtag::getName)
                .collect(Collectors.toList());
    }
}
