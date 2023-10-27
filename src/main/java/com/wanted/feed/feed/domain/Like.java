package com.wanted.feed.feed.domain;

import com.wanted.feed.exception.feed.like.LikeFeedIdNotNullException;
import com.wanted.feed.exception.feed.like.LikeUserIdNotNullException;
import com.wanted.feed.feed.domain.vo.FeedId;
import com.wanted.feed.feed.domain.vo.UserId;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Table(name = "likes")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FeedId feedId;

    @Embedded
    private UserId userId;

    @Builder
    public Like(FeedId feedId, UserId userId) {
        validateFeedAndUserIdsNotNull(feedId, userId);
        this.feedId = feedId;
        this.userId = userId;
    }

    private void validateFeedAndUserIdsNotNull(FeedId feedId, UserId userId) {
        if (Objects.isNull(feedId)) {
            throw new LikeFeedIdNotNullException();
        }

        if (Objects.isNull(userId)) {
            throw new LikeUserIdNotNullException();
        }
    }
}
