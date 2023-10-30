package com.wanted.feed.feed.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wanted.feed.feed.domain.vo.FeedId;
import com.wanted.feed.feed.domain.vo.UserId;
import com.wanted.feed.feed.exception.share.ShareFeedIdNotNullException;
import com.wanted.feed.feed.exception.share.ShareUserIdNotNullException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Share 엔티티 테스트")
class ShareTest {

    @Test
    @DisplayName("Builder 를 이용해 Share 객체 생성 - 성공")
    void createShareWithBuilder() {
        assertThatCode(() -> Share.builder()
                .userId(new UserId(1L))
                .feedId(new FeedId(1L))
                .build()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Builder 로 객체 생성 시 UserId 가 Null 이면 예외를 던진다 - 실패")
    void throwExceptionWhenUserIdIsNull() {
        assertThatThrownBy(() -> Share.builder()
                .userId(null)
                .feedId(new FeedId(1L))
                .build()).isInstanceOf(ShareUserIdNotNullException.class);
    }

    @Test
    @DisplayName("Builder 로 객체 생성 시 FeedId 가 Null 이면 예외를 던진다 - 실패")
    void throwExceptionWhenFeedIdIsNull() {
        assertThatThrownBy(() -> Share.builder()
                .userId(new UserId(1L))
                .feedId(null)
                .build()).isInstanceOf(ShareFeedIdNotNullException.class);
    }
}