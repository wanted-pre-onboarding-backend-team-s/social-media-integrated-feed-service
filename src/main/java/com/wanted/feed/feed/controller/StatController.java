package com.wanted.feed.feed.controller;

import com.wanted.feed.common.response.ApiResponse;
import com.wanted.feed.feed.dto.StatRequestParamDto;
import com.wanted.feed.feed.dto.StatResponseDto;
import com.wanted.feed.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "통계")
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatController {

    private final FeedService feedService;

    @Operation(summary = "피드 및 해시태그 관련 통계 조회")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<Map<String, StatResponseDto>> stats(
            @RequestAttribute("userId") Long userId,

            // TODO: ModelAttribute 적용
            StatRequestParamDto statRequestParamDto) {
        statRequestParamDto.validate();
        return ApiResponse.toResponse(feedService.getFeedStats(userId, statRequestParamDto));
    }
}
