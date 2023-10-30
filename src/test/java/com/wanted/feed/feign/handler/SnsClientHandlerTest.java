package com.wanted.feed.feign.handler;

import static org.assertj.core.api.Assertions.*;

import com.wanted.feed.feign.exception.SnsNotSupportException;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feign.SnsClient;
import com.wanted.feed.feign.TwitterClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SnsClientHandlerTest {

    @Autowired
    SnsClientHandler snsClientHandler;

    @Test
    @DisplayName("Feed 에 알맞은 client 를 반환한다. - 성공")
    void getSnsClientByFeed() {
        Feed feed = Feed.builder()
                .type("twitter")
                .content("content")
                .title("title")
                .build();

        SnsClient client = snsClientHandler.getSnsClientByFeed(feed);
        assertThat(client).isInstanceOf(TwitterClient.class);
    }

    @Test
    @DisplayName("지원하지않은 sns 타입의 경우 에러를 던진다. - 실패")
    void throwExceptionWhenNoSupportSnsType() {
        Feed feed = Feed.builder()
                .type("kakao")
                .content("content")
                .title("title")
                .build();

        assertThatThrownBy(() -> snsClientHandler.getSnsClientByFeed(feed))
                .isInstanceOf(SnsNotSupportException.class);
    }
}
