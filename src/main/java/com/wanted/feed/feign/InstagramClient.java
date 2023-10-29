package com.wanted.feed.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "instagram", url = "https://www.instagram.com")
public interface InstagramClient extends SnsClient {
}

