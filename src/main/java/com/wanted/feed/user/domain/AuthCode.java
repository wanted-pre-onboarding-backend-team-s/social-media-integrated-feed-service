package com.wanted.feed.user.domain;

import com.wanted.feed.common.domain.entity.BaseCreateTimeEntity;
import com.wanted.feed.user.exception.MismatchAuthCodeException;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "auth_codes")
@EntityListeners(AuditingEntityListener.class)
public class AuthCode extends BaseCreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String username;

    @Builder
    public AuthCode(String code, String username) {
        this.code = code;
        this.username = username;
    }

    public void checkAuthCodeMatches(String code) {
        if (!this.getCode().equals(code)) {
            throw new MismatchAuthCodeException();
        }
    }
}
