package com.wanted.feed.feed.domain;

import com.wanted.feed.common.domain.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "feeds")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String type;
    private String title;
    @Lob
    private String content;
    private Integer viewCount;
    private Integer shareCount;
    private Integer likeCount;
    private String contentId;

    @Builder
    public Feed(String type, String title, String content,
            Integer viewCount, Integer shareCount, Integer likeCount,
            String contentId, LocalDateTime createdAt) {
        super(createdAt);

        this.type = type;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount != null ? viewCount : 0;
        this.shareCount = shareCount != null ? shareCount : 0;
        this.likeCount = likeCount != null ? likeCount : 0;
        this.contentId = contentId;
    }

    public void updateViews() {
        this.viewCount++;
    }

    public void updateShares() {
        this.shareCount++;
    }

    public void updateLikes() {
        this.likeCount++;
    }

    public String getType() {
        return type;
    }
}
