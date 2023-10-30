package com.wanted.feed.common.domain.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseCreateTimeEntity {

    @CreatedDate
    private LocalDateTime createdAt;

    protected BaseCreateTimeEntity(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}