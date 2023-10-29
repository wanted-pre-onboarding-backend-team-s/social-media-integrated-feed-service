package com.wanted.feed.feed.dto;

import com.wanted.feed.common.response.PagedResponse;
import com.wanted.feed.common.response.Pagination;
import com.wanted.feed.feed.domain.Feed;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class FeedResponseDto {

    private static final int MAX_CONTENT_LENGTH = 20;

    private String type;
    private String title;
    private String content;
    private int viewCount;
    private String contentId;

    public static FeedResponseDto of(Feed feed) {
        String shortContent = toShortContent(feed.getContent());

        return new FeedResponseDto(
            feed.getType(),
            feed.getTitle(),
            shortContent,
            feed.getViewCount(),
            feed.getContentId()
        );
    }

    public static List<FeedResponseDto> listOf(List<Feed> feeds) {
        return feeds.stream()
            .map(FeedResponseDto::of)
            .collect(Collectors.toList());
    }

    public static PagedResponse<FeedResponseDto> pagedListOf(Pagination pagination,
        Page<Feed> feeds) {
        List<FeedResponseDto> feedResponseDtoList = feeds.stream()
            .map(FeedResponseDto::of)
            .toList();

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

}