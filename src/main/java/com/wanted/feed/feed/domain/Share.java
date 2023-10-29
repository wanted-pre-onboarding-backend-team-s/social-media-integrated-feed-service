package com.wanted.feed.feed.domain;

import com.wanted.feed.common.domain.entity.BaseCreateTimeEntity;
import com.wanted.feed.feed.domain.vo.FeedId;
import com.wanted.feed.feed.domain.vo.UserId;
import com.wanted.feed.feed.exception.share.ShareFeedIdNotNullException;
import com.wanted.feed.feed.exception.share.ShareUserIdNotNullException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Table(name = "shares")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Share extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private FeedId feedId;
    private UserId userId;

    @Builder
    public Share(FeedId feedId, UserId userId) {
        validateFeedAndUserIdsNotNull(feedId, userId);
        this.feedId = feedId;
        this.userId = userId;
    }

    private void validateFeedAndUserIdsNotNull(FeedId feedId, UserId userId) {
        if (Objects.isNull(feedId)) {
            throw new ShareFeedIdNotNullException();
        }

        if (Objects.isNull(userId)) {
            throw new ShareUserIdNotNullException();
        }
    }
}
