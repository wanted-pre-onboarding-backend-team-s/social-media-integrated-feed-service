package com.wanted.feed.user.dto;

import lombok.Getter;

@Getter
public class JoinResponseDto {
    private String username;
    private String code;

    public JoinResponseDto(String username, String code) {
        this.username = username;
        this.code = code;
    }
}
