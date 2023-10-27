package com.wanted.feed.user.controller;

import com.wanted.feed.common.response.ApiResponse;
import com.wanted.feed.user.dto.LoginRequestDto;
import com.wanted.feed.user.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Controller
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> Login(@RequestAttribute(required = false) Long userId, @Validated @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {

        log.debug("login userId = {}", userId);
        if (userId == null) {
            return ResponseEntity.created(URI.create("/feeds"))
                    .body(new ApiResponse(HttpStatus.CREATED.value(), loginService.getLoginAuthorization(loginRequestDto))
            );
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
            headers.setLocation(URI.create("/feeds"));
            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Long> feeds(@RequestAttribute Long userId) {
        log.debug("test 접근");
        return ResponseEntity.ok(userId);
    }
}
