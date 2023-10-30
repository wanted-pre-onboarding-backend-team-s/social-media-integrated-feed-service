package com.wanted.feed.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.wanted.feed.common.exception.ErrorType;
import com.wanted.feed.feed.domain.Feed;
import com.wanted.feed.feed.domain.FeedRepository;
import com.wanted.feed.feed.domain.Hashtag;
import com.wanted.feed.feed.domain.HashtagRepository;
import com.wanted.feed.feed.dto.FeedDetailResponseDto;
import com.wanted.feed.feed.dto.StatRequestParamDto;
import com.wanted.feed.feed.dto.StatResponseDto;
import com.wanted.feed.feed.exception.DateRangeExceeds30Days;
import com.wanted.feed.feed.exception.DateRangeExceeds7Days;
import com.wanted.feed.feed.exception.FeedNotFoundException;
import com.wanted.feed.feed.exception.StartIsAfterEndException;
import com.wanted.feed.user.domain.User;
import com.wanted.feed.user.domain.UserRepository;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @InjectMocks
    private FeedService feedService;

    @Mock
    private FeedRepository feedRepository;

    @Mock
    private HashtagRepository hashtagRepository;

    @Mock
    private UserRepository userRepository;

    @DisplayName("findFeedDetail")
    @Nested
    class FindFeedDetail {

        private static final Long FEED_ID = 1L;
        private static final int FEED_DEFAULT_VIEW_COUNT = 0;

        Feed feed;

        @BeforeEach
        void setUp() throws NoSuchFieldException, IllegalAccessException {
            feed = Feed.builder()
                    .content("content")
                    .type("twitter")
                    .title("title")
                    .contentId("contentId")
                    .build();

            Field field = Feed.class.getDeclaredField("id");
            field.setAccessible(true); // 필드에 접근 가능하도록 설정
            field.set(feed, 1L);
        }

        @DisplayName("피드 상세 조회시 피드가 존재하지 않으면 예외를 던진다 - 실패")
        @Test
        void find_feed_detail_failed_not_found() {
            when(feedRepository.findById(FEED_ID)).thenReturn(Optional.empty());

            final FeedNotFoundException result = assertThrows(
                    FeedNotFoundException.class,
                    () -> feedService.findFeedDetail(FEED_ID)
            );

            assertThat(result.getErrorType()).isEqualTo(ErrorType.F001);
        }

        @DisplayName("피드 상세 조회 - 성공")
        @Test
        void find_feed_detail_success() {
            when(feedRepository.findById(FEED_ID)).thenReturn(Optional.of(feed));
            doReturn(Arrays.asList(
                    Hashtag.builder().name("tag1").build(),
                    Hashtag.builder().name("tag2").build()
            )).when(hashtagRepository).findHashTagsByFeedId(FEED_ID);

            final FeedDetailResponseDto result = feedService.findFeedDetail(FEED_ID);

            assertThat(result.getTitle()).isEqualTo("title");
            assertThat(result.getHashtag().size()).isEqualTo(2);
        }

        @DisplayName("피드 상세 조회시 조회수가 1 증가한다 - 성공")
        @Test
        void find_feed_detail_update_view_count() {
            doReturn(Optional.of(feed)).when(feedRepository).findById(FEED_ID);

            final FeedDetailResponseDto result = feedService.findFeedDetail(FEED_ID);

            assertThat(result.getViewCount()).isEqualTo(FEED_DEFAULT_VIEW_COUNT + 1);
        }
    }

    @DisplayName("getFeedStats")
    @Nested
    class GetFeedStats {

        private static final Long USER_ID = 1L;
        private static final String USERNAME = "username";

        @DisplayName("메서드")
        @Nested
        class Methods {

            @DisplayName("toDateTimeToFeeds")
            @Nested
            class ToDateTimeToFeeds {

                @DisplayName("List<Feed>를 Map<날짜, List<Feed>>로 변환")
                @Test
                void toDateToFeeds() {
                    List<Feed> feeds = List.of(
                            Feed.builder().createdAt(LocalDateTime.of(2023, 10, 15, 1, 0, 0))
                                    .build(),
                            Feed.builder().createdAt(LocalDateTime.of(2023, 10, 15, 16, 0, 0))
                                    .build(),
                            Feed.builder().createdAt(LocalDateTime.of(2023, 10, 15, 23, 50, 0))
                                    .build(),
                            Feed.builder().createdAt(LocalDateTime.of(2023, 10, 16, 12, 0, 0))
                                    .build()
                    );
                    String type = "date";
                    LocalDateTime start = LocalDateTime.of(2023, 10, 15, 0, 0, 0);
                    LocalDateTime end = LocalDateTime.of(2023, 10, 16, 23, 59, 59);

                    Map<LocalDateTime, List<Feed>> dateToFeeds = feedService
                            .toDateTimeToFeeds(feeds, type, start, end);

                    assertThat(dateToFeeds).isNotNull();
                    assertThat(dateToFeeds).hasSize(2);

                    assertThat(dateToFeeds.get(LocalDateTime.of(2023, 10, 15, 0, 0, 0))).hasSize(3);
                    assertThat(dateToFeeds.get(LocalDateTime.of(2023, 10, 16, 0, 0, 0))).hasSize(1);
                }

                @DisplayName("List<Feed>를 Map<시간, List<Feed>>로 변환")
                @Test
                void toHourToFeeds() {
                    List<Feed> feeds = List.of();
                    String type = "hour";
                    LocalDateTime start = LocalDateTime.of(2023, 10, 15, 0, 0, 0);
                    LocalDateTime end = LocalDateTime.of(2023, 10, 16, 23, 59, 59);

                    Map<LocalDateTime, List<Feed>> dateToFeeds = feedService
                            .toDateTimeToFeeds(feeds, type, start, end);

                    assertThat(dateToFeeds).isNotNull();
                    assertThat(dateToFeeds).hasSize(48);
                }
            }

            @DisplayName("toDateTimeToStatResponseDtos")
            @Nested
            class ToDateTimeToStatResponseDtos {

                @DisplayName("Map<시간, List<Feed>>를 Map<\"yyyy-mm-dd hh:mm\", StatResponseDto>로 변환")
                @Test
                void toHourToStatResponseDtos() {
                    Map<LocalDateTime, List<Feed>> hourToFeeds = Map.of(
                            LocalDateTime.of(2023, 10, 15, 14, 0, 0), List.of(
                                    Feed.builder()
                                            .viewCount(10).shareCount(2).likeCount(3)
                                            .createdAt(LocalDateTime.of(2023, 10, 15, 14, 30, 0))
                                            .build(),
                                    Feed.builder()
                                            .viewCount(10).shareCount(2).likeCount(3)
                                            .createdAt(LocalDateTime.of(2023, 10, 21, 14, 50, 0))
                                            .build()
                            ),
                            LocalDateTime.of(2023, 10, 15, 15, 0, 0), List.of(Feed.builder()
                                    .viewCount(10).shareCount(2).likeCount(3)
                                    .createdAt(LocalDateTime.of(2023, 10, 21, 15, 30, 0))
                                    .build()
                            )
                    );
                    String type = "hour";
                    List<String> valueSelectors = List.of("count", "share_count");

                    Map<String, StatResponseDto> hourToStatResponseDto = feedService
                            .toDateTimeToStatResponseDtos(hourToFeeds, type, valueSelectors);

                    assertThat(hourToStatResponseDto).isNotNull();
                    StatResponseDto first = hourToStatResponseDto.get("2023-10-15 14:00");
                    assertThat(first.getCount()).isEqualTo(2);
                    assertThat(first.getViewCount()).isNull();
                    assertThat(first.getShareCount()).isEqualTo(4);
                    assertThat(first.getLikeCount()).isNull();

                    StatResponseDto second = hourToStatResponseDto.get("2023-10-15 15:00");
                    assertThat(second.getCount()).isEqualTo(1);
                    assertThat(second.getShareCount()).isEqualTo(2);
                }
            }
        }

        @DisplayName("RequestParam 정상")
        @Nested
        class ValidRequestParams {

            @DisplayName("Map<\"yyyy-mm-dd\", StatResponseDto> 반환")
            @Test
            void getFeedStats() {
                User user = User.builder()
                        .username(USERNAME)
                        .build();
                given(userRepository.findById(USER_ID))
                        .willReturn(Optional.of(user));

                List<Feed> feeds = List.of(
                        Feed.builder()
                                .viewCount(10).shareCount(2).likeCount(2)
                                .createdAt(LocalDateTime.of(2023, 10, 18, 16, 0, 0))
                                .build(),
                        Feed.builder()
                                .viewCount(10).shareCount(2).likeCount(2)
                                .createdAt(LocalDateTime.of(2023, 10, 21, 23, 50, 0))
                                .build(),
                        Feed.builder()
                                .viewCount(10).shareCount(2).likeCount(2)
                                .createdAt(LocalDateTime.of(2023, 10, 21, 23, 50, 0))
                                .build()
                );
                LocalDateTime start = LocalDateTime.of(2023, 10, 16, 0, 0, 0);
                LocalDateTime end = LocalDateTime.of(2023, 10, 22, 23, 59, 59);
                given(feedRepository
                        .findAllByHashtagAndCreatedAtBetweenStartAndEnd(USERNAME, start, end))
                        .willReturn(feeds);

                StatRequestParamDto statRequestParamDto = StatRequestParamDto.builder()
                        .type("date")
                        .start(start.toLocalDate())
                        .end(end.toLocalDate())
                        .value(List.of("count", "view_count"))
                        .build();
                Map<String, StatResponseDto> dateToStatResponseDtos = feedService
                        .getFeedStats(USER_ID, statRequestParamDto);

                assertThat(dateToStatResponseDtos).isNotNull();
            }
        }

        @DisplayName("잘못된 RequestParam 존재")
        @Nested
        class InvalidRequestParams {

            @DisplayName("시작일이 종료일보다 앞설 경우 예외 발생")
            @Test
            void throwStartIsAfterEndException() {
                StatRequestParamDto statRequestParamDto = StatRequestParamDto.builder()
                        .start(LocalDate.of(2023, 10, 15))
                        .end(LocalDate.of(2023, 10, 12))
                        .build();

                assertThrows(StartIsAfterEndException.class, () -> feedService
                        .getFeedStats(USER_ID, statRequestParamDto));
            }

            @DisplayName("타입이 date이고 두 날짜 간의 차이가 30을 초과하면 예외 발생")
            @Test
            void throwDateRangeExceeds30Days() {
                StatRequestParamDto statRequestParamDto = StatRequestParamDto.builder()
                        .type("date")
                        .start(LocalDate.of(2023, 10, 15))
                        .end(LocalDate.of(2023, 11, 15))
                        .build();

                assertThrows(DateRangeExceeds30Days.class, () -> feedService
                        .getFeedStats(USER_ID, statRequestParamDto));
            }

            @DisplayName("타입이 hour이고 두 날짜 간의 차이가 7을 초과하면 예외 발생")
            @Test
            void throwDateRangeExceeds7Days() {
                StatRequestParamDto statRequestParamDto = StatRequestParamDto.builder()
                        .type("hour")
                        .start(LocalDate.of(2023, 10, 15))
                        .end(LocalDate.of(2023, 10, 23))
                        .build();

                assertThrows(DateRangeExceeds7Days.class, () -> feedService
                        .getFeedStats(USER_ID, statRequestParamDto));
            }
        }
    }
}
