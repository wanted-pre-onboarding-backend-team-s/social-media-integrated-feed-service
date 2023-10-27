package com.wanted.feed.feign;

import com.wanted.feed.exception.client.SnsContentIdNotNullException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

public interface SocialMediaClient {

    @PutMapping("/likes/{contentId}")
    default ResponseEntity<String> likeFeed(@PathVariable String contentId) {
        verificationNullContentId(contentId);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/shares/{contentId}")
    default ResponseEntity<String> shareFeed(@PathVariable String contentId) {
        verificationNullContentId(contentId);
        return ResponseEntity.ok("ok");
    }

    private static void verificationNullContentId(String contentId) {
        if (contentId == null) {
            throw new SnsContentIdNotNullException();
        }
    }
}
