package com.wanted.feed.feed.domain;

import com.wanted.feed.feed.dto.SearchFeedRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedRepositoryCustom {

    Page<Feed> findFeedListBySearch(SearchFeedRequestDto filter, Pageable pageable);

}
