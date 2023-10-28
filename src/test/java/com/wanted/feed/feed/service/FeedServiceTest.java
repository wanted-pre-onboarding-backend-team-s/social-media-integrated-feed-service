package com.wanted.feed.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

import com.wanted.feed.exception.ErrorType;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.FeedRepository;
import com.wanted.feed.feed.dto.FeedDetailResponseDto;
import com.wanted.feed.feed.exception.FeedNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    private static final Long FEED_ID = 1L;

    private static final String FEED_TYPE = "spring";

    private static final String FEED_TITLE = "피드제목입니다";

    private static final int FEED_VIEW_COUNT = 1;

    private static final String FEED_CONTENT = "콘텐츠입니다";

    private static final String FEED_CONTENT_ID = "1E1D1F";

    @InjectMocks
    private FeedService feedService;

    @Mock
    private FeedRepository feedRepository;

    @DisplayName("피드 상세 조회 실패 존재하지 않음")
    @Test
    void find_feed_detail_failed_not_found() {
        doReturn(Optional.empty()).when(feedRepository).findById(FEED_ID);

        final FeedNotFoundException result = assertThrows(
            FeedNotFoundException.class,
            () -> feedService.findFeedDetail(FEED_ID)
        );

        assertThat(result.getErrorType()).isEqualTo(ErrorType.F001);
    }

}