package com.wanted.feed.feed.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wanted.feed.feed.exception.like.LikeFeedIdNotNullException;
import com.wanted.feed.feed.exception.like.LikeUserIdNotNullException;
import com.wanted.feed.feed.domain.vo.FeedId;
import com.wanted.feed.feed.domain.vo.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("'Like' 엔티티 테스트")
class LikeTest {

    @Test
    @DisplayName("Builder 를 이용하여 Like 를 객체를 만든다. - 성공")
    void createLikeWithBuilder() {
        assertThatCode(() -> Like.builder()
                .userId(new UserId(1L))
                .feedId(new FeedId(1L))
                .build()).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Like 생성 시 UserId가 없으면 예외를 던진다. - 실패")
    void throwExceptionWhenUserIdNotInLike() {
        assertThatThrownBy(() -> Like.builder()
                .userId(null)
                .feedId(new FeedId(1L))
                .build())
                .isInstanceOf(LikeUserIdNotNullException.class);
    }

    @Test
    @DisplayName("Like 생성 시 FeedId가 없으면 예외를 던진다. - 실패")
    void throwExceptionWhenFeedIdNotInLike() {
        assertThatThrownBy(() -> Like.builder()
                .userId(new UserId(1L))
                .feedId(null)
                .build())
                .isInstanceOf(LikeFeedIdNotNullException.class);
    }

    @Test
    @DisplayName("UserId 생성 시 id 가 없으면 예외를 던진다. - 실패")
    void throwExceptionWhenIdNotInUserId() {
        assertThatThrownBy(() -> new UserId(null))
                .isInstanceOf(LikeUserIdNotNullException.class);
    }

    @Test
    @DisplayName("FeedId 생성 시 id 가 없으면 예외를 던진다. - 실패")
    void throwExceptionWhenIdNotInFeedId() {
        assertThatThrownBy(() -> new FeedId(null))
                .isInstanceOf(LikeFeedIdNotNullException.class);
    }
}
