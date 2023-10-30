package com.wanted.feed.feed.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.wanted.feed.common.config.jpa.JpaQueryFactoryTestConfig;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(JpaQueryFactoryTestConfig.class)
@ActiveProfiles("test")
class FeedRepositoryImplTest {

    @Autowired
    private FeedRepositoryImpl repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM feeds_hashtags");
        jdbcTemplate.execute("DELETE FROM feeds");
        jdbcTemplate.execute("DELETE FROM hashtags");

        jdbcTemplate.update("""
                        INSERT INTO feeds(id, type, title, content,
                        view_count, like_count, share_count, content_id,
                        created_at)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?),
                        (?, ?, ?, ?, ?, ?, ?, ?, ?),
                        (?, ?, ?, ?, ?, ?, ?, ?, ?),
                        (?, ?, ?, ?, ?, ?, ?, ?, ?)""",

                1L, "facebook", "제목1", "내용1",
                10, 1, 1, "FACEBOOK_ID_1",
                Timestamp.valueOf(LocalDateTime.of(2023, 10, 15, 14, 0, 0)),

                2L, "instagram", "제목2", "내용2",
                5, 1, 1, "INSTAGRAM_ID_1",
                Timestamp.valueOf(LocalDateTime.of(2023, 10, 16, 14, 0, 0)),

                3L, "twitter", "제목3", "내용3",
                2, 1, 1, "TWITTER_ID_1",
                Timestamp.valueOf(LocalDateTime.of(2023, 10, 16, 22, 0, 0)),

                4L, "threads", "제목4", "내용4",
                1, 1, 1, "THREADS_ID_1",
                Timestamp.valueOf(LocalDateTime.of(2023, 10, 17, 2, 0, 0))
        );

        jdbcTemplate.update("""
                        INSERT INTO hashtags(id, name)
                        VALUES (?, ?),
                        (?, ?)""",

                1L, "원티드",
                2L, "개발자연봉"
        );

        jdbcTemplate.update("""
                        INSERT INTO feeds_hashtags(id, feed_id, hashtag_id)
                        VALUES (?, ?, ?),
                        (?, ?, ?),
                        (?, ?, ?),
                        (?, ?, ?),
                        (?, ?, ?)""",

                1L, 1L, 1L,
                2L, 2L, 1L,
                3L, 4L, 1L,
                4L, 2L, 2L,
                5L, 3L, 2L
        );
    }

    @DisplayName("해시태그를 포함하면서 시작일, 종료일 이내의 피드들을 쿼리")
    @Test
    void findAllByHashtagAndCreatedAtBetweenStartAndEnd() {
        String hashtag = "원티드";
        LocalDateTime start = LocalDateTime.of(2023, 10, 15, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 10, 16, 23, 59, 59);

        List<Feed> feeds = repository
                .findAllByHashtagAndCreatedAtBetweenStartAndEnd(hashtag, start, end);

        assertThat(feeds).isNotNull();
        assertThat(feeds.size()).isEqualTo(2);

        List<String> typeIncluded = List.of("facebook", "instagram");
        feeds.forEach(feed -> assertThat(typeIncluded).contains(feed.getType()));
    }
}
