package com.wanted.feed.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "facebook", url = "https://www.facebook.com")
public interface FacebookClient extends SocialMediaClient{
}