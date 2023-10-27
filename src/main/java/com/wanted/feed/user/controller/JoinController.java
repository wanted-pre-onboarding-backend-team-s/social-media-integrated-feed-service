package com.wanted.feed.user.controller;

import com.wanted.feed.common.response.ApiResponse;
import com.wanted.feed.user.dto.ApprovalRequestDto;
import com.wanted.feed.user.dto.JoinRequestDto;
import com.wanted.feed.user.dto.JoinResponseDto;
import com.wanted.feed.user.service.JoinService;
import com.wanted.feed.user.validation.ValidationSequence;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/join")
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping
    public ApiResponse<JoinResponseDto> join(
            @Validated(ValidationSequence.class) @RequestBody JoinRequestDto joinRequest) {
        return ApiResponse.toResponse(joinService.join(joinRequest));
    }

    @PutMapping
    public ApiResponse<Void> approve(
            @Valid @RequestBody ApprovalRequestDto approvalRequest) {
        joinService.approve(approvalRequest);
        return ApiResponse.ok();
    }
}
