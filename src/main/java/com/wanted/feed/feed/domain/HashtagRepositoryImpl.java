package com.wanted.feed.feed.domain;

import static com.wanted.feed.feed.domain.QFeedToHashtag.feedToHashtag;
import static com.wanted.feed.feed.domain.QHashtag.hashtag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HashtagRepositoryImpl implements HashtagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Hashtag> findHashTagsByFeedId(Long feedId) {
        return jpaQueryFactory.selectFrom(hashtag)
            .join(feedToHashtag).on(hashtag.id.eq(feedToHashtag.hashtagId))
            .where(feedToHashtag.feedId.eq(feedId))
            .fetch();
    }

}