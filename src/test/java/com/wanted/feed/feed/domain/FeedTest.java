package com.wanted.feed.feed.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("'Feed' 엔티티 테스트")
class FeedTest {

    @Test
    @DisplayName("Builder 를 이용하여 Feed 객체를 만든다. - 성공")
    void createFeedWithBuilder() {
        assertThatCode(() -> Feed.builder()
            .title("title")
            .type("twitter")
            .content("content")
            .contentId("1234")
            .build()).doesNotThrowAnyException();
    }

}