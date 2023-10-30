package com.wanted.feed.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(name = "LoginRequestDto", title = "Login Request", description = "로그인 인증 요청 Object")
public class LoginRequestDto {

    @Schema(title = "user name", description = "사용자 계정")
    @NotBlank(message = "필수값 입니다")
    @Size(min = 2, message = "2글자 이상 입력해주세요")
    private String username;

    @Schema(title = "password", description = "사용자 비밀번호")
    @NotBlank(message = "필수값 입니다")
    @Size(min = 10, message = "10글자 이상 입력해주세요")
    private String password;

    @Builder
    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
