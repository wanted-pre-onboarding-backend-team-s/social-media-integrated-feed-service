package com.wanted.feed.feed.dto;

import com.wanted.feed.common.validator.ValidEnum;
import com.wanted.feed.feed.domain.enums.FeedOrderByType;
import com.wanted.feed.feed.domain.enums.FeedSearchByType;
import com.wanted.feed.feed.domain.enums.SortDirectionType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class SearchFeedRequestDto {

    private String hashtag;

    private String type;

    @ValidEnum(enumClass = FeedOrderByType.class)
    private FeedOrderByType order_by;

    @ValidEnum(enumClass = FeedSearchByType.class)
    private FeedSearchByType search_by;

    @ValidEnum(enumClass = SortDirectionType.class)
    private SortDirectionType sort_direction;

    private String search;

    private Integer page_count;

    private Integer page;

}