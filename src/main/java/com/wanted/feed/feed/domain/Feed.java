package com.wanted.feed.feed.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Table(name = "feeds")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    @Lob
    private String title;

    private String content;

    private int viewCount;

    private String contentId;

    @Builder
    public Feed(String type, String title, String content, String contentId) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.contentId = contentId;
    }
}
