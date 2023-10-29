package com.wanted.feed.feed.controller;

import com.wanted.feed.common.response.ApiResponse;
import com.wanted.feed.feed.dto.FeedShareResponseDto;
import com.wanted.feed.feed.service.FeedShareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드")
@RestController
public class FeedShareController {

    private final FeedShareService feedShareService;

    public FeedShareController(FeedShareService feedShareService) {
        this.feedShareService = feedShareService;
    }

    @Operation(summary = "피드 공유 하기")
    @PostMapping("/feeds/{id}/share")
    public ApiResponse<FeedShareResponseDto> shareFeed(@PathVariable Long id) {
        return ApiResponse.toResponse(feedShareService.shareFeed(1L, id));
    }
}
