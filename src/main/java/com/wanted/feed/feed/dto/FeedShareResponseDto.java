package com.wanted.feed.feed.dto;

import com.wanted.feed.feign.dto.response.ClientShareResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedShareResponseDto {

    private final String url;

    @Builder
    private FeedShareResponseDto(String url) {
        this.url = url;
    }

    public static FeedShareResponseDto of(ClientShareResponseDto clientShareResponseDto) {
        return FeedShareResponseDto.builder()
                .url(clientShareResponseDto.getFeedUrl())
                .build();
    }
}
