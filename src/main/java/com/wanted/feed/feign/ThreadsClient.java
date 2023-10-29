package com.wanted.feed.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "threads", url = "https://www.threads.com")
public interface ThreadsClient extends SnsClient {
}
