package com.wanted.feed.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wanted.feed.exception.client.SnsShareFeedFailException;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.FeedRepository;
import com.wanted.feed.feed.domain.Share;
import com.wanted.feed.feed.domain.ShareRepository;
import com.wanted.feed.feign.SnsClient;
import com.wanted.feed.feign.dto.response.ClientShareResponseDto;
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
class FeedShareServiceTest {

    @Mock
    FeedRepository feedRepository;

    @Mock
    SnsClient snsClient;

    @Mock
    ShareRepository shareRepository;

    @Mock
    SnsClientHandler snsClientHandler;

    @InjectMocks
    FeedShareService feedShareService;

    Feed feed;

    ClientShareResponseDto clientResponseDto;

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

        clientResponseDto = ClientShareResponseDto.builder()
                .feedUrl("feedUrl")
                .build();
    }

    @Test
    @DisplayName("피드 공유 서비스 테스트 - 성공")
    void sendFeeShareServiceTest() {
        // given
        when(feedRepository.findById(any())).thenReturn(Optional.of(feed));
        when(snsClientHandler.getSnsClientByFeed(feed)).thenReturn(snsClient);
        when(snsClient.shareFeed(feed.getContentId())).thenReturn(
                ResponseEntity.ok(clientResponseDto));

        // when
        feedShareService.shareFeed(1L, 1L);

        // then
        assertThat(feed.getShareCount()).isEqualTo(1L);
        verify(shareRepository, times(1)).save(any(Share.class));
    }

    @Test
    @DisplayName("외부 SNS 공유 서비스의 요청 응답이 200이 아니면 예외를 던진다 - 실패")
    void throwExceptionSnsFail() {
        // given
        when(feedRepository.findById(any())).thenReturn(Optional.of(feed));
        when(snsClientHandler.getSnsClientByFeed(feed)).thenReturn(snsClient);
        when(snsClient.shareFeed(feed.getContentId())).thenReturn(
                ResponseEntity.badRequest().body(clientResponseDto));

        // when, then
        assertThatThrownBy(() -> feedShareService.shareFeed(1L, 1L))
                .isInstanceOf(SnsShareFeedFailException.class);
        assertThat(feed.getShareCount()).isEqualTo(0);
    }
}