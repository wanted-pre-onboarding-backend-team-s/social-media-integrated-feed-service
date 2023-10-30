package com.wanted.feed.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtResponse {

    // 토큰
    private String token;

    // 발급 시간
    private String issuedTime;

    // 만료 시간
    private String expiredTime;

    @Builder
    public JwtResponse(String token, String issuedTime, String expiredTime) {
        this.token = token;
        this.issuedTime = issuedTime;
        this.expiredTime = expiredTime;
    }
}
