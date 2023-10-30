package com.wanted.feed.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtResponse {

    private String token;

    private String issuedTime;

    private String expiredTime;

    @Builder
    public JwtResponse(String token, String issuedTime, String expiredTime) {
        this.token = token;
        this.issuedTime = issuedTime;
        this.expiredTime = expiredTime;
    }
}
