package com.wanted.feed.feed.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wanted.feed.exception.client.SnsLikeFeedFailException;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.FeedRepository;
import com.wanted.feed.feed.domain.Like;
import com.wanted.feed.feed.domain.LikeRepository;
import com.wanted.feed.feign.SnsClient;
import com.wanted.feed.feign.handler.SnsClientHandler;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.ResponseEntity;

@MockitoSettings
class FeedLikeServiceTest {

    @Mock
    FeedRepository feedRepository;

    @Mock
    SnsClient snsClient;

    @Mock
    LikeRepository likeRepository;

    @Mock
    SnsClientHandler snsClientHandler;

    @InjectMocks
    FeedLikeService feedLikeService;

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

    @Test
    @DisplayName("피드 좋아요 저장하기 - 성공")
    void sendFeeLikeServiceTest() {
        // given
        when(feedRepository.findById(any())).thenReturn(Optional.of(feed));
        when(snsClientHandler.getSnsClientByFeed(feed)).thenReturn(snsClient);
        when(snsClient.likeFeed(feed.getContentId())).thenReturn(ResponseEntity.ok("ok"));

        // when
        feedLikeService.sendFeedLike(1L, 1L);

        // then
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    @Test
    @DisplayName("외부 SNS 좋아요 서비스의 요청 응답이 200이 아니면 예외를 던진다 - 실패")
    void throwExceptionSnsFail() {
        // given
        when(feedRepository.findById(any())).thenReturn(Optional.of(feed));
        when(snsClientHandler.getSnsClientByFeed(feed)).thenReturn(snsClient);
        when(snsClient.likeFeed(feed.getContentId())).thenReturn(
                ResponseEntity.badRequest().body("ok"));


        // when, then
        assertThatThrownBy(() -> feedLikeService.sendFeedLike(1L, 1L))
                .isInstanceOf(SnsLikeFeedFailException.class);
    }
}
