package com.wanted.feed.feed.controller;

import com.wanted.feed.common.response.ApiResponse;
import com.wanted.feed.feed.service.FeedLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드")
@RestController
public class FeedLikeController {

    private final FeedLikeService feedLikeService;

    public FeedLikeController(FeedLikeService feedLikeService) {
        this.feedLikeService = feedLikeService;
    }

    @Operation(description = "피드 좋아요 요청")
    @PostMapping("/feeds/{id}/like")
    public ApiResponse<Void> likeFeed(
            @RequestAttribute("userId") Long userId,
            @PathVariable Long id) {
        feedLikeService.sendFeedLike(userId, id);
        return ApiResponse.ok();
    }
}
