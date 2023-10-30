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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

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

    // TODO::hashTag 값 없을 시 본인계정 값으로 업데이트
    public PagedResponse<FeedResponseDto> findFeedsBySearch(
        SearchFeedRequestDto searchFeedRequest) {
        Pagination pagination = Pagination.create(searchFeedRequest.getPage(),
            searchFeedRequest.getPage_count());
        PageRequest pageRequest = pagination.toPageRequest();

        Page<Feed> feedListBySearch = feedRepository.findFeedListBySearch(
            searchFeedRequest, pageRequest);
        return FeedResponseDto.pagedListOf(pagination, feedListBySearch);
    }

}
