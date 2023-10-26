package com.wanted.feed.feed.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Table(name = "likes")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue
    private Long id;
    private Long feedId;
    private Long userId;

    @Builder
    public Like(Long feedId, Long userId) {
        this.feedId = feedId;
        this.userId = userId;
    }
}
