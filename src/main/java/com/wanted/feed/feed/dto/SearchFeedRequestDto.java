package com.wanted.feed.feed.dto;

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

    private String type;

    private String order_by;

    private String search_by;

    private String search;

    private Integer page_count;

    private Integer page;

}