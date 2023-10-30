package com.wanted.feed.feed.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public final class StatResponseDto {

    private Long count;
    private Long viewCount;
    private Long likeCount;
    private Long shareCount;

    public static StatResponseDto specifyValues(List<String> values) {
        Long count = values.contains("count") ? 0L : null;
        Long viewCount = values.contains("view_count") ? 0L : null;
        Long likeCount = values.contains("like_count") ? 0L : null;
        Long shareCount = values.contains("share_count") ? 0L : null;

        return new StatResponseDto(count, viewCount, likeCount, shareCount);
    }

    public void addCount() {
        if (count == null) {
            return;
        }
        this.count += 1L;
    }

    public void addViewCount(Long viewCount) {
        if (this.viewCount == null) {
            return;
        }
        this.viewCount += viewCount;
    }

    public void addLikeCount(Long likeCount) {
        if (this.likeCount == null) {
            return;
        }
        this.likeCount += likeCount;
    }

    public void addShareCount(Long shareCount) {
        if (this.shareCount == null) {
            return;
        }
        this.shareCount += shareCount;
    }
}
