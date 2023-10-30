package com.wanted.feed.feed.service;

import com.wanted.feed.exception.client.SnsLikeFeedFailException;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.FeedRepository;
import com.wanted.feed.feed.domain.Like;
import com.wanted.feed.feed.domain.LikeRepository;
import com.wanted.feed.feed.domain.vo.FeedId;
import com.wanted.feed.feed.domain.vo.UserId;
import com.wanted.feed.feign.SnsClient;
import com.wanted.feed.feign.handler.SnsClientHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedLikeService {

    private final FeedRepository feedRepository;
    private final LikeRepository likeRepository;
    private final SnsClientHandler snsClientHandler;

    public FeedLikeService(FeedRepository feedRepository, LikeRepository likeRepository,
            SnsClientHandler snsClientHandler) {
        this.feedRepository = feedRepository;
        this.likeRepository = likeRepository;
        this.snsClientHandler = snsClientHandler;
    }

    @Transactional
    public void sendFeedLike(Long userId, Long feedId) {
        Feed feed = findFeed(feedId);
        SnsClient client = snsClientHandler.getSnsClientByFeed(feed);
        ResponseEntity<String> response = client.likeFeed(feed.getContentId());
        if (isFail(response)) {
            throw new SnsLikeFeedFailException();
        }
        Like like = createLike(userId, feedId);
        likeRepository.save(like);
    }

    private static Like createLike(Long userId, Long feedId) {
        return Like.builder()
                .userId(new UserId(userId))
                .feedId(new FeedId(feedId))
                .build();
    }

    private boolean isFail(ResponseEntity<String> response) {
        return !response.getStatusCode().is2xxSuccessful();
    }

    private Feed findFeed(long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow();
    }

}
