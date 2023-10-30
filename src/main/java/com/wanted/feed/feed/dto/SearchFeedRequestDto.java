package com.wanted.feed.feed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("order_by")
    private FeedOrderByType orderBy;

    @ValidEnum(enumClass = FeedSearchByType.class)
    @JsonProperty("search_by")
    private FeedSearchByType searchBy;

    @ValidEnum(enumClass = SortDirectionType.class)
    @JsonProperty("sort_direction")
    private SortDirectionType sortDirection;

    private String search;

    @JsonProperty("page_count")
    private Integer pageCount;

    private Integer page;

}