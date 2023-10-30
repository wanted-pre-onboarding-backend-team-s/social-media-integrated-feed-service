package com.wanted.feed.feed.domain.vo;

import com.wanted.feed.feed.exception.like.LikeUserIdNotNullException;
import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId {

    private Long userId;

    public UserId(Long userId) {
        verifyNotNull(userId);
        this.userId = userId;
    }

    private void verifyNotNull(Long userId) {
        if (Objects.isNull(userId)) {
            throw new LikeUserIdNotNullException();
        }
    }
}
