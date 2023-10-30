package com.wanted.feed.feign;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.wanted.feed.feign.exception.SnsContentIdNotNullException;
import com.wanted.feed.feign.dto.response.ClientShareResponseDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class SnsClientTest {

    @Autowired
    List<SnsClient> clients;

    @Test
    @DisplayName("외부로 나가는 모든 SNS 좋아요 요청 응답의 상태값은 200 - 성공")
    void likeFeedTest() {
        List<HttpStatusCode> resultCodes = clients.stream()
                .map(client -> client.likeFeed("contentId").getStatusCode())
                .toList();

        assertThat(resultCodes).containsOnly(HttpStatusCode.valueOf(200));
    }

    @Test
    @DisplayName("외부 SNS 좋아요 요청 시 contentId 가 Null 이면 예외를 던진다. - 실패")
    void throwExceptionWhenContentIdIsNull() {
        for (SnsClient client : clients) {
            assertThatThrownBy(() -> client.likeFeed(null))
                    .isInstanceOf(SnsContentIdNotNullException.class);
        }
    }

    @Test
    @DisplayName("SNS 공유 요청시 공유 링크와 함께 상태값 200 반환 - 성공")
    void sharedFeedTest() {
        List<ResponseEntity<ClientShareResponseDto>> responseDtos = clients.stream()
                .map(client -> client.shareFeed("contentId"))
                .toList();

        assertThat(responseDtos).hasSize(clients.size());
        assertThat(responseDtos).allSatisfy(responseEntity -> {
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
            assertThat(responseEntity.getBody()).isNotNull();
            assertThat(responseEntity.getBody().getFeedUrl()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("외부 SNS 공유 요청 시 contentId 가 Null 이면 예외를 던진다. - 실패")
    void throwExceptionWhenShareContentIdIsNull() {
        for (SnsClient client : clients) {
            assertThatThrownBy(() -> client.shareFeed(null))
                    .isInstanceOf(SnsContentIdNotNullException.class);
        }
    }
}
