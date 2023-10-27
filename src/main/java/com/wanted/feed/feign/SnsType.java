package com.wanted.feed.feign;

import com.wanted.feed.exception.client.SnsNotSupportException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SnsType {
    TWITTER("twitter", TwitterClient.class),
    INSTAGRAM("instagram", InstagramClient.class),
    THREADS("threads", ThreadsClient.class),
    FACEBOOK("facebook", FacebookClient.class);

    private final String snsType;
    private final Class<? extends SocialMediaClient> classType;

    public static SnsType findType(String snsType) {
        return Arrays.stream(SnsType.values())
                .filter(type -> type.snsType.equalsIgnoreCase(snsType))
                .findFirst()
                .orElseThrow(SnsNotSupportException::new);
    }
}
