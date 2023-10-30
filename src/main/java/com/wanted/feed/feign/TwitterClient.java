package com.wanted.feed.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "twitter", url = "https://www.twitter.com")
public interface TwitterClient extends SnsClient {
}
