package com.wanted.feed.feed.controller;

import com.wanted.feed.common.response.ApiResponse;
import com.wanted.feed.feed.service.FeedLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "feed-like", description = "피드 좋아요 API")
@RestController
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    public FeedLikeController(FeedLikeService feedLikeService) {
        this.feedLikeService = feedLikeService;
    }

    @PostMapping("/feeds/{id}/like")
    @Operation(description = "피드 좋아요 요청")
    public ApiResponse<Void> likeFeed(@PathVariable Long id) {
        feedLikeService.sendFeedLike(1L, id);
        return ApiResponse.ok();
    }
}
