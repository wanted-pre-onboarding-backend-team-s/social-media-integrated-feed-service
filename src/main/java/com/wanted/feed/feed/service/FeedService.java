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
import com.wanted.feed.feed.dto.StatRequestParamDto;
import com.wanted.feed.feed.dto.StatResponseDto;
import com.wanted.feed.feed.exception.DateRangeExceeds30Days;
import com.wanted.feed.feed.exception.DateRangeExceeds7Days;
import com.wanted.feed.feed.exception.FeedNotFoundException;
import com.wanted.feed.feed.exception.StartIsAfterEndException;
import com.wanted.feed.user.domain.UserRepository;
import com.wanted.feed.user.exception.NotFoundUserException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private static final LocalTime ZERO_O_CLOCK = LocalTime.of(0, 0, 0);
    private static final LocalTime BEFORE_THE_HOUR = LocalTime.of(23, 59, 59);

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

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
                searchFeedRequest.getPageCount());
        PageRequest pageRequest = pagination.toPageRequest();

        Page<Feed> feedListBySearch = feedRepository.findFeedListBySearch(
                searchFeedRequest, pageRequest);

        Map<Long, List<Hashtag>> hashtagsMap = hashtagRepository.findHashtagMapByFeeds(
                feedListBySearch.toList()
        );
        return FeedResponseDto.pagedListOf(pagination, feedListBySearch, hashtagsMap);
    }

    @Transactional(readOnly = true)
    public Map<String, StatResponseDto> getFeedStats(Long userId,
            StatRequestParamDto statRequestParamDto) {
        LocalDateTime start = getStart(statRequestParamDto);
        LocalDateTime end = getEnd(statRequestParamDto);

        validateDateOrder(start, end);

        String type = statRequestParamDto.getType();
        long difference = ChronoUnit.DAYS.between(start.toLocalDate(), end.toLocalDate());

        validateDateDifference(type, difference);

        String hashtag = getHashtag(userId, statRequestParamDto);
        List<Feed> feeds = feedRepository
                .findAllByHashtagAndCreatedAtBetweenStartAndEnd(hashtag, start, end);

        Map<LocalDateTime, List<Feed>> dateTimeToFeeds = toDateTimeToFeeds(feeds, type, start, end);

        List<String> valueSelectors = statRequestParamDto.getValue()
                .stream()
                .filter(value -> !value.isBlank())
                .distinct()
                .collect(Collectors.toList());

        if (valueSelectors.isEmpty()) {
            valueSelectors.add("count");
        }

        return toDateTimeToStatResponseDtos(dateTimeToFeeds, type, valueSelectors);
    }

    private LocalDateTime getStart(StatRequestParamDto statRequestParamDto) {
        return statRequestParamDto.getStart() != null
                ? LocalDateTime.of(statRequestParamDto.getStart(), ZERO_O_CLOCK)
                : LocalDateTime.of(LocalDate.now(), ZERO_O_CLOCK).minusDays(7);
    }

    private LocalDateTime getEnd(StatRequestParamDto statRequestParamDto) {
        return statRequestParamDto.getEnd() != null
                ? LocalDateTime.of(statRequestParamDto.getEnd(), BEFORE_THE_HOUR)
                : LocalDateTime.of(LocalDate.now(), BEFORE_THE_HOUR);
    }

    // TODO: ModelAttribute를 통한 RequestParam 바인딩,
    //       단 Validation 적용이 가능하면 이 로직들은 여기서 그대로 예외처리 수행
    private void validateDateOrder(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new StartIsAfterEndException();
        }
    }

    private void validateDateDifference(String type, long difference) {
        if (type.equals("date") && difference > 30) {
            throw new DateRangeExceeds30Days();
        }
        if (type.equals("hour") && difference > 7) {
            throw new DateRangeExceeds7Days();
        }
    }

    private String getHashtag(Long userId, StatRequestParamDto statRequestParamDto) {
        return statRequestParamDto.getHashtag() != null
                ? statRequestParamDto.getHashtag()
                : userRepository.findById(userId)
                        .orElseThrow(NotFoundUserException::new)
                        .getUsername();
    }

    public Map<LocalDateTime, List<Feed>> toDateTimeToFeeds(
            List<Feed> feeds, String type, LocalDateTime start, LocalDateTime end) {
        Map<LocalDateTime, List<Feed>> dateTimeToFeeds = new LinkedHashMap<>();

        for (LocalDateTime time = start;
                time.isBefore(end);
                time = type.equals("date") ? time.plusDays(1L) : time.plusHours(1L)) {
            dateTimeToFeeds.put(time, new ArrayList<>());
        }

        feeds.forEach(feed -> dateTimeToFeeds
                .get(LocalDateTime.of(feed.getCreatedAt().toLocalDate(), ZERO_O_CLOCK))
                .add(feed));

        return dateTimeToFeeds;
    }

    public Map<String, StatResponseDto> toDateTimeToStatResponseDtos(
            Map<LocalDateTime, List<Feed>> dateTimeToFeeds,
            String type,
            List<String> valueSelectors
    ) {
        return dateTimeToFeeds.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> {
                            LocalDateTime dateTime = entry.getKey();
                            DateTimeFormatter formatter = type.equals("date")
                                    ? DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                    : DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                            return dateTime.format(formatter);
                        },
                        entry -> {
                            List<Feed> feeds = entry.getValue();
                            StatResponseDto statResponseDto = StatResponseDto
                                    .specifyValues(valueSelectors);
                            feeds.forEach(feed -> {
                                statResponseDto.addCount();
                                statResponseDto.addViewCount((long) feed.getViewCount());
                                statResponseDto.addLikeCount((long) feed.getLikeCount());
                                statResponseDto.addShareCount((long) feed.getShareCount());
                            });
                            return statResponseDto;
                        }
                ));
    }
}
