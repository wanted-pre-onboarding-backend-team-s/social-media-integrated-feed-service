package com.wanted.feed.feed.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortDirectionType {
    ASC("정방향"),
    DESC("역방향");

    private final String name;

}
