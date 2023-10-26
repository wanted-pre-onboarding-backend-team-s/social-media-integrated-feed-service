package com.wanted.feed.feed.domain.vo;

import com.wanted.feed.exception.feed.like.LikeFeedIdNotNullException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedId {

    private Long feedId;

    public FeedId(Long feedId) {
        verifyNotNull(feedId);
        this.feedId = feedId;
    }

    private void verifyNotNull(Long feedId) {
        if (Objects.isNull(feedId)) {
            throw new LikeFeedIdNotNullException();
        }
    }
}
