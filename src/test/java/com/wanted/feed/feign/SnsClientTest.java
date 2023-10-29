package com.wanted.feed.feign;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wanted.feed.exception.client.SnsContentIdNotNullException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

@SpringBootTest
class SnsClientTest {

    @Autowired
    List<SnsClient> clients;

    @Test
    @DisplayName("외부로 나가는 모든 SNS 좋아요 요청 응답의 상태값은 200")
    void likeFeedTest() {
        List<HttpStatusCode> resultCodes = clients.stream()
                .map(client -> client.likeFeed("contentId").getStatusCode())
                .toList();

        assertThat(resultCodes).containsOnly(HttpStatusCode.valueOf(200));
    }

    @Test
    @DisplayName("외부 SNS 좋아요 요청 시 contentId 가 Null 이면 예외를 던진다.")
    void throwExceptionWhenContentIdIsNull() {
        for (SnsClient client : clients) {
            assertThatThrownBy(() -> client.likeFeed(null))
                    .isInstanceOf(SnsContentIdNotNullException.class);
        }
    }
}