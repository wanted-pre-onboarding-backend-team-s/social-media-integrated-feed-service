package com.wanted.feed.feed.domain;

import com.wanted.feed.common.domain.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
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
    private int viewCount;
    private int likeCount;
    private int shareCount;
    private String contentId;

    @Builder
    public Feed(String type, String title, String content,
        String contentId) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.likeCount = 0;
        this.shareCount = 0;
        this.contentId = contentId;
    }

    public void updateViews() {
        this.viewCount++;
    }

    public void updateLikes() {
        this.likeCount++;
    }

    public void updateShares() {
        this.shareCount++;
    }

}
