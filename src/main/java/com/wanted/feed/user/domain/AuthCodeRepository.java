package com.wanted.feed.user.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    Optional<AuthCode> findTopByUsernameOrderByCreatedAtDesc(String username);
}
