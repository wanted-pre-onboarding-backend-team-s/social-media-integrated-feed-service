package com.wanted.feed.feign;

import com.wanted.feed.feign.dto.response.ClientShareResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

@FeignClient(value = "threads", url = "https://www.threads.com")
public interface ThreadsClient extends SnsClient {

    @Override
    default ResponseEntity<ClientShareResponseDto> shareFeed(String contentId) {
        this.verificationNullContentId(contentId);
        return ResponseEntity.ok(ClientShareResponseDto.builder()
                .feedUrl("https://www.threads.com/feeds/" + contentId)
                .build());
    }
}
