package com.wanted.feed.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "필수값 입니다")
    @Size(min = 2, message = "2글자 이상 입력해주세요")
    private String username;

    @NotBlank(message = "필수값 입니다")
    @Size(min = 10, message = "10글자 이상 입력해주세요")
    private String password;
}
