package com.wanted.feed.feed.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hashtags")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Builder
    public Hashtag(String name) {
        this.name = name;
    }

}
