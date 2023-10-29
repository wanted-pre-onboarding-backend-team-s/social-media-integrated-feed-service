package com.wanted.feed.feign;

import com.wanted.feed.feign.dto.response.ClientShareResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

@FeignClient(value = "instagram", url = "https://www.instagram.com")
public interface InstagramClient extends SnsClient {

    @Override
    default ResponseEntity<ClientShareResponseDto> shareFeed(String contentId) {
        this.verificationNullContentId(contentId);
        return ResponseEntity.ok(ClientShareResponseDto.builder()
                .feedUrl("https://www.instagram.com/" + contentId)
                .build());
    }
}

