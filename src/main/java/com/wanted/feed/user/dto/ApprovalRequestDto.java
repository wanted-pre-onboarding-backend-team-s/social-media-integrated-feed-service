package com.wanted.feed.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApprovalRequestDto {

    @NotBlank(message = "필수입니다.")
    private String username;

    @NotBlank(message = "필수입니다.")
    private String password;

    @NotBlank(message = "필수입니다.")
    private String code;

    public ApprovalRequestDto(String username, String password, String code) {
        this.username = username;
        this.password = password;
        this.code = code;
    }
}
