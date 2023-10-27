package com.wanted.feed.feed.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "feeds_hashtags")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FeedToHashtag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "feed_id")
    private Long feedId;

    @Column(name = "hashtag_id")
    private Long hashtagId;
}
