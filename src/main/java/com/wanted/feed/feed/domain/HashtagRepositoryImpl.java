package com.wanted.feed.feed.domain;

import static com.wanted.feed.feed.domain.QFeedToHashtag.feedToHashtag;
import static com.wanted.feed.feed.domain.QHashtag.hashtag;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    @Override
    public Map<Long, List<Hashtag>> findHashtagMapByFeeds(List<Feed> feeds) {
        List<Long> feedIds = getFeedIds(feeds);
        List<Tuple> results = jpaQueryFactory
            .select(feedToHashtag.feedId, hashtag)
            .from(feedToHashtag)
            .join(hashtag).on(feedToHashtag.hashtagId.eq(hashtag.id))
            .where(feedToHashtag.feedId.in(feedIds))
            .fetch();

        return results.stream()
            .collect(Collectors.groupingBy(
                result -> result.get(feedToHashtag.feedId),
                Collectors.mapping(result -> result.get(hashtag), Collectors.toList())
            ));
    }

    private List<Long> getFeedIds(List<Feed> feeds) {
        return feeds.stream().map(Feed::getId).toList();
    }
}
