package com.wanted.feed.feed.service;

import com.wanted.feed.common.response.PagedResponse;
import com.wanted.feed.common.response.Pagination;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.FeedRepository;
import com.wanted.feed.feed.domain.Hashtag;
import com.wanted.feed.feed.domain.HashtagRepository;
import com.wanted.feed.feed.dto.FeedDetailResponseDto;
import com.wanted.feed.feed.dto.FeedResponseDto;
import com.wanted.feed.feed.dto.SearchFeedRequestDto;
import com.wanted.feed.feed.exception.FeedNotFoundException;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.exception.NotFoundUserException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final UserRepository userRepository;

    private final FeedRepository feedRepository;

    private final HashtagRepository hashtagRepository;

    @Transactional
    public FeedDetailResponseDto findFeedDetail(Long id) {
        Feed feed = findFeedById(id);

        List<Hashtag> hashtagList = hashtagRepository.findHashTagsByFeedId(id);
        feed.updateViews();
        return FeedDetailResponseDto.of(feed, hashtagList);
    }

    @Transactional(readOnly = true)
    public Feed findFeedById(Long id) {
        return feedRepository.findById(id)
            .orElseThrow(FeedNotFoundException::new);
    }

    public PagedResponse<FeedResponseDto> findFeedsBySearch(
        Long userId, SearchFeedRequestDto searchFeedRequest) {
        User loginUser = userRepository.findById(userId).orElseThrow(
            NotFoundUserException::new);

        Pagination pagination = Pagination.create(searchFeedRequest.getPage(),
            searchFeedRequest.getPageCount());
        PageRequest pageRequest = pagination.toPageRequest();

        Page<Feed> feedListBySearch = feedRepository.findFeedListBySearch(loginUser,
            searchFeedRequest, pageRequest);

        Map<Long, List<Hashtag>> hashtagsMap = hashtagRepository.findHashtagMapByFeeds(
            feedListBySearch.toList()
        );
        return FeedResponseDto.pagedListOf(pagination, feedListBySearch, hashtagsMap);
    }

}
