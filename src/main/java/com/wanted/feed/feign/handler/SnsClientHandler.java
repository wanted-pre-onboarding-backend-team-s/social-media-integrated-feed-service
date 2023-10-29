package com.wanted.feed.feign.handler;

import com.wanted.feed.exception.client.SnsNotSupportException;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feign.SnsType;
import com.wanted.feed.feign.SnsClient;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SnsClientHandler {

    private final List<SnsClient> clients;


    public SnsClientHandler(List<SnsClient> clients) {
        this.clients = clients;
    }

    public SnsClient getSnsClientByFeed(Feed feed) {
        return clients.stream()
                .filter(client -> SnsType.findType(feed.getType()).getClassType()
                        .isAssignableFrom(client.getClass()))
                .findFirst()
                .orElseThrow(SnsNotSupportException::new);
    }
}
