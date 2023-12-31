package com.wanted.feed.feed.domain;

import com.wanted.feed.feed.dto.SearchFeedRequestDto;
import java.time.LocalDateTime;
import java.util.List;
import com.wanted.feed.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedRepositoryCustom {

    Page<Feed> findFeedListBySearch(User loginUser, SearchFeedRequestDto filter, Pageable pageable);

    List<Feed> findAllByHashtagAndCreatedAtBetweenStartAndEnd(
            String hashtag, LocalDateTime start, LocalDateTime end);
}
