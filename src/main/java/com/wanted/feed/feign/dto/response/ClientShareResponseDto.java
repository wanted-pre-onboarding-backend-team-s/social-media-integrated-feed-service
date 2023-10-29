package com.wanted.feed.feign.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ClientShareResponseDto {

    private String feedUrl;

    @Builder
    public ClientShareResponseDto(String feedUrl) {
        this.feedUrl = feedUrl;
    }
}
