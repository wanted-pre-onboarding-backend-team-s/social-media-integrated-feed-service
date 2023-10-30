package com.wanted.feed.feed.controller;

import com.wanted.feed.common.response.ApiResponse;
import com.wanted.feed.common.response.PagedResponse;
import com.wanted.feed.feed.dto.FeedDetailResponseDto;
import com.wanted.feed.feed.dto.FeedResponseDto;
import com.wanted.feed.feed.dto.SearchFeedRequestDto;
import com.wanted.feed.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "피드")
@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @Operation(summary = "피드 목록 조회")
    @GetMapping("/filter")
    public ApiResponse<PagedResponse<FeedResponseDto>> findFeedsBySearch(
        @Valid @ParameterObject @ModelAttribute SearchFeedRequestDto searchFeedRequest
    ) {
        return ApiResponse.toResponse(feedService.findFeedsBySearch(searchFeedRequest));
    }

    @Operation(summary = "피드 상세 조회")
    @GetMapping("/detail/{id}")
    public ApiResponse<FeedDetailResponseDto> findFeedDetail(
        @PathVariable Long id
    ) {
        return ApiResponse.toResponse(feedService.findFeedDetail(id));
    }

}