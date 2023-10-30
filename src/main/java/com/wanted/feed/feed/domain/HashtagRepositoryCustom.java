package com.wanted.feed.feed.domain;

import java.util.List;

public interface HashtagRepositoryCustom {

    List<Hashtag> findHashTagsByFeedId(Long feedId);

}
