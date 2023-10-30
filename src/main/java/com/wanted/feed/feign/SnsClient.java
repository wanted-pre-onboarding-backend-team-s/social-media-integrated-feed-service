package com.wanted.feed.feign;

import com.wanted.feed.feign.exception.SnsContentIdNotNullException;
import com.wanted.feed.feign.dto.response.ClientShareResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

public interface SnsClient {

    @PutMapping("/likes/{contentId}")
    default ResponseEntity<String> likeFeed(@PathVariable String contentId) {
        verificationNullContentId(contentId);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/shares/{contentId}")
    ResponseEntity<ClientShareResponseDto> shareFeed(@PathVariable String contentId);

    default void verificationNullContentId(String contentId) {
        if (contentId == null) {
            throw new SnsContentIdNotNullException();
        }
    }
}
