package com.wanted.feed.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.wanted.feed.common.exception.ErrorType;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.FeedRepository;
import com.wanted.feed.feed.domain.Hashtag;
import com.wanted.feed.feed.domain.HashtagRepository;
import com.wanted.feed.feed.dto.FeedDetailResponseDto;
import com.wanted.feed.feed.exception.FeedNotFoundException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    private static final Long FEED_ID = 1L;
    private static final int FEED_DEFAULT_VIEW_COUNT = 0;

    @InjectMocks
    private FeedService feedService;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private HashtagRepository hashtagRepository;

    Feed feed;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        feed = Feed.builder()
            .content("content")
            .type("twitter")
            .title("title")
            .contentId("contentId")
            .build();

        Field field = Feed.class.getDeclaredField("id");
        field.setAccessible(true); // 필드에 접근 가능하도록 설정
        field.set(feed, 1L);
    }

    @DisplayName("피드 상세 조회시 피드가 존재하지 않으면 예외를 던진다 - 실패")
    @Test
    void find_feed_detail_failed_not_found() {
        when(feedRepository.findById(FEED_ID)).thenReturn(Optional.empty());

        final FeedNotFoundException result = assertThrows(
            FeedNotFoundException.class,
            () -> feedService.findFeedDetail(FEED_ID)
        );

        assertThat(result.getErrorType()).isEqualTo(ErrorType.F001);
    }

    @DisplayName("피드 상세 조회 - 성공")
    @Test
    void find_feed_detail_success() {
        when(feedRepository.findById(FEED_ID)).thenReturn(Optional.of(feed));
        doReturn(Arrays.asList(
            Hashtag.builder().name("tag1").build(),
            Hashtag.builder().name("tag2").build()
        )).when(hashtagRepository).findHashTagsByFeedId(FEED_ID);

        final FeedDetailResponseDto result = feedService.findFeedDetail(FEED_ID);

        assertThat(result.getTitle()).isEqualTo("title");
        assertThat(result.getHashtag().size()).isEqualTo(2);
    }

    @DisplayName("피드 상세 조회시 조회수가 1 증가한다 - 성공")
    @Test
    void find_feed_detail_update_view_count() {
        doReturn(Optional.of(feed)).when(feedRepository).findById(FEED_ID);

        final FeedDetailResponseDto result = feedService.findFeedDetail(FEED_ID);

        assertThat(result.getViewCount()).isEqualTo(FEED_DEFAULT_VIEW_COUNT + 1);
    }

}