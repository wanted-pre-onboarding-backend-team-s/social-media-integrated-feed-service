package com.wanted.feed.feed.dto;

import com.wanted.feed.common.response.PagedResponse;
import com.wanted.feed.common.response.Pagination;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.Hashtag;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedResponseDto {

    private static final int MAX_CONTENT_LENGTH = 20;

    private List<String> hashtag;
    private String type;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int shareCount;
    private String contentId;

    @Builder
    public FeedResponseDto(List<String> hashtag, String type, String title, String content,
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

    public static FeedResponseDto of(Feed feed, List<Hashtag> hashtagList) {
        List<String> hashtags = toHashtags(hashtagList);
        String shortContent = toShortContent(feed.getContent());

        return FeedResponseDto.builder()
            .hashtag(hashtags)
            .type(feed.getType())
            .content(shortContent)
            .viewCount(feed.getViewCount())
            .likeCount(feed.getLikeCount())
            .shareCount(feed.getShareCount())
            .contentId(feed.getContentId())
            .build();
    }

    public static PagedResponse<FeedResponseDto> pagedListOf(Pagination pagination,
        Page<Feed> feeds, Map<Long, List<Hashtag>> hashtagsMap) {
        List<FeedResponseDto> feedResponseDtoList = feeds.stream()
            .map(feed -> FeedResponseDto.of(
                feed,
                hashtagsMap.getOrDefault(feed.getId(), Collections.emptyList())
            )).toList();

        pagination.setTotalCount(feeds.getTotalElements());
        pagination.setTotalPages(feeds.getTotalPages());

        return PagedResponse.of(
            pagination,
            feedResponseDtoList
        );
    }

    private static String toShortContent(String content) {
        if (content.length() <= MAX_CONTENT_LENGTH) {
            return content;
        }

        return content.substring(0, MAX_CONTENT_LENGTH);
    }

    private static List<String> toHashtags(List<Hashtag> hashtagList) {
        return hashtagList.stream()
            .map(Hashtag::getName)
            .collect(Collectors.toList());
    }

}