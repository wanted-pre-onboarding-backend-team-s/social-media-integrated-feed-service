package com.wanted.feed.feign.config;

import com.wanted.feed.feign.FacebookClient;
import com.wanted.feed.feign.InstagramClient;
import com.wanted.feed.feign.SnsClient;
import com.wanted.feed.feign.ThreadsClient;
import com.wanted.feed.feign.TwitterClient;
import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocialMediaClientConfig {

    final FacebookClient facebookClient;
    final InstagramClient instagramClient;
    final ThreadsClient threadsClient;
    final TwitterClient twitterClient;

    public SocialMediaClientConfig(FacebookClient facebookClient, InstagramClient instagramClient,
            ThreadsClient threadsClient, TwitterClient twitterClient) {
        this.facebookClient = facebookClient;
        this.instagramClient = instagramClient;
        this.threadsClient = threadsClient;
        this.twitterClient = twitterClient;
    }

    @Bean
    public List<SnsClient> clients() {
        return Arrays.asList(facebookClient, instagramClient, threadsClient, twitterClient);
    }
}
