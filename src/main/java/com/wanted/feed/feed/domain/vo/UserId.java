package com.wanted.feed.feed.domain.vo;

import com.wanted.feed.exception.feed.like.LikeUserIdNotNullException;
import jakarta.persistence.Column;
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
