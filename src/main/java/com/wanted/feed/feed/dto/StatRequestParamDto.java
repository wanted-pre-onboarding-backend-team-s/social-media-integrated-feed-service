package com.wanted.feed.feed.dto;

import com.wanted.feed.feed.exception.EndDateInvalidException;
import com.wanted.feed.feed.exception.StartDateInvalidException;
import com.wanted.feed.feed.exception.TypeInvalidException;
import com.wanted.feed.feed.exception.TypeNullException;
import com.wanted.feed.feed.exception.ValueInvalidException;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@Getter
public class StatRequestParamDto {

    private String hashtag;

    private String type;

    private LocalDate start;

    private LocalDate end;

    private List<String> value;

    public void validate() {
        if (type == null || type.isBlank()) {
            throw new TypeNullException();
        }

        if (!type.matches("^(date|hour)$")) {
            throw new TypeInvalidException();
        }

        if (start != null && !start.toString().matches("^(\\d{4}-\\d{2}-\\d{2})$")) {
            throw new StartDateInvalidException();
        }

        if (end != null && !end.toString().matches("^(\\d{4}-\\d{2}-\\d{2})$")) {
            throw new EndDateInvalidException();
        }

        if (value == null || value.isEmpty()) {
            return;
        }
        value.stream()
                .filter(value -> !value.isBlank())
                .distinct()
                .forEach(value -> {
                    if (!value.matches("^(count|view_count|like_count|share_count)$")) {
                        throw new ValueInvalidException();
                    }
                });
    }
}
