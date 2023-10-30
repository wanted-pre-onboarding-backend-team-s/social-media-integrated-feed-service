package com.wanted.feed.user.controller;

import com.wanted.feed.common.response.ApiResponse;
import com.wanted.feed.user.dto.LoginRequestDto;
import com.wanted.feed.user.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
@Tag(name = "Login", description = "로그인 관련")
public class LoginController {

    private final LoginService loginService;

    @Operation(summary = "로그인 요청", description = "로그인 인증, 토큰 발급")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @Validated @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.created(URI.create("/feeds"))
                .body(new ApiResponse(HttpStatus.CREATED.value(),
                        loginService.getLoginAuthorization(
                                loginService.getAuthenticatedByLogin(loginRequestDto))));
    }
}
