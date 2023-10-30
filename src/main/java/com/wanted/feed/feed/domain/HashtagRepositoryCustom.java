package com.wanted.feed.feed.domain;

import java.util.List;
import java.util.Map;

public interface HashtagRepositoryCustom {

    List<Hashtag> findHashTagsByFeedId(Long feedId);

    Map<Long, List<Hashtag>> findHashtagMapByFeeds(List<Feed> feeds);

}
