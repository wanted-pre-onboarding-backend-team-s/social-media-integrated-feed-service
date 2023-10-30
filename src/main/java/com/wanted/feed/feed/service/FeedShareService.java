package com.wanted.feed.feed.service;

import com.wanted.feed.feign.exception.SnsShareFeedFailException;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.FeedRepository;
import com.wanted.feed.feed.domain.Share;
import com.wanted.feed.feed.domain.ShareRepository;
import com.wanted.feed.feed.domain.vo.FeedId;
import com.wanted.feed.feed.domain.vo.UserId;
import com.wanted.feed.feed.dto.FeedShareResponseDto;
import com.wanted.feed.feed.exception.FeedNotFoundException;
import com.wanted.feed.feign.SnsClient;
import com.wanted.feed.feign.dto.response.ClientShareResponseDto;
import com.wanted.feed.feign.handler.SnsClientHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedShareService {

    private final ShareRepository shareRepository;
    private final FeedRepository feedRepository;
    private final SnsClientHandler snsClientHandler;

    public FeedShareService(ShareRepository shareRepository, FeedRepository feedRepository,
            SnsClientHandler snsClientHandler) {
        this.shareRepository = shareRepository;
        this.feedRepository = feedRepository;
        this.snsClientHandler = snsClientHandler;
    }

    @Transactional
    public FeedShareResponseDto shareFeed(long userId, long feedId) {
        Feed feed = findFeed(feedId);

        SnsClient client = snsClientHandler.getSnsClientByFeed(feed);
        ResponseEntity<ClientShareResponseDto> clientResponse = client.shareFeed(
                feed.getContentId());
        if (isFail(clientResponse)) {
            throw new SnsShareFeedFailException();
        }

        feed.updateShares();
        saveShare(userId, feedId);
        return FeedShareResponseDto.of(clientResponse.getBody());
    }

    private void saveShare(long userId, long feedId) {
        Share share = createShare(userId, feedId);
        shareRepository.save(share);
    }

    private static Share createShare(long userId, long feedId) {
        return Share.builder()
                .userId(new UserId(userId))
                .feedId(new FeedId(feedId))
                .build();
    }

    private static boolean isFail(ResponseEntity<ClientShareResponseDto> clientResponse) {
        return !clientResponse.getStatusCode().is2xxSuccessful();
    }

    private Feed findFeed(long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(FeedNotFoundException::new);
    }
}
