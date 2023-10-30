package com.wanted.feed.feed.dto;

import com.wanted.feed.common.response.PagedResponse;
import com.wanted.feed.common.response.Pagination;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.Hashtag;
import java.util.List;
import java.util.Map;
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

    private List<String> hashtag;
    private String type;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;
    private int shareCount;
    private String contentId;

    public static FeedResponseDto of(Feed feed, List<Hashtag> hashtagList) {
        List<String> hashtags = toHashtags(hashtagList);
        String shortContent = toShortContent(feed.getContent());

        return new FeedResponseDto(
            hashtags,
            feed.getType(),
            feed.getTitle(),
            shortContent,
            feed.getViewCount(),
            feed.getLikeCount(),
            feed.getShareCount(),
            feed.getContentId()
        );
    }

    public static PagedResponse<FeedResponseDto> pagedListOf(Pagination pagination,
        Page<Feed> feeds, Map<Long, List<Hashtag>> hashtagsMap) {
        List<FeedResponseDto> feedResponseDtoList = feeds.stream()
            .map(feed -> FeedResponseDto.of(feed, hashtagsMap.get(feed.getId())))
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

    private static List<String> toHashtags(List<Hashtag> hashtagList) {
        return hashtagList.stream()
            .map(Hashtag::getName)
            .collect(Collectors.toList());
    }

}